package it.nerdammer.spash.shell;

import it.nerdammer.spash.shell.command.Command;
import it.nerdammer.spash.shell.command.CommandFactory;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.session.SpashSession;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * A java shell that emulates bash.
 *
 * @author Nicola Ferraro
 */
public class SpashShell implements org.apache.sshd.server.Command, Runnable {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private SpashSession session;

    private InputStream inputStream;
    private OutputStream outputStream;
    private OutputStream errorStream;
    private ExitCallback exitCallback;

    private Thread managerTread;

    public SpashShell() {
    }

    public void setInputStream(final InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setErrorStream(OutputStream errorStream) {
        this.errorStream = errorStream;
    }

    public void setExitCallback(ExitCallback exitCallback) {
        this.exitCallback = exitCallback;
    }

    public void start(Environment environment) throws IOException {

        this.session = new SpashSession(environment.getEnv().get(Environment.ENV_USER));

        this.managerTread = new Thread(this);
        this.managerTread.start();
    }

    public void destroy() {
        this.managerTread.interrupt();
        try {
            this.managerTread.join();
        } catch(InterruptedException e) {
            // nothing to do
        }

        exitCallback.onExit(0);
    }

    public void run() {
        try {

            PrintWriter err = new PrintWriter(errorStream, true);
            PrintWriter out = new PrintWriter(outputStream, true);


            out.print("Welcome to Spash 0.1 (Spark 1.6.0, Hadoop 2.6.0)\r\n");
            out.print("\r\n");
            writeHeading(out);
            out.flush();


            StringBuilder builder = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(this.inputStream);
            int read;
            do {
                read = reader.read();
                switch (read) {
                    case 10:
                    case 13:

                        out.append("\r\n");

                        String commandStr = builder.toString();
                        Command command = CommandFactory.getInstance().getCommand(commandStr);

                        CommandResult result = command.execute(this.session);

                        if(result.isSuccess()) {
                            result.getContent().mkString(out);
                        } else {
                            out.print(result.getErrorMessage());
                            out.print("\r\n");
                        }


                        builder.delete(0, builder.length());
                        writeHeading(out);

                        break;
                    case 3:
                        this.destroy();

                        break;
                    default:
                        char ch = (char) read;
                        builder.append(ch);
                        out.append(ch);
                }

                out.flush();
            } while (read >= 0);

        } catch(InterruptedIOException ie) {
            logger.info("Logger console closed");
        } catch (Exception e) {
            logger.error("Logger console closed abnormally", e);
            exitCallback.onExit(-1);
        }
    }

    private void writeHeading(PrintWriter writer) {
        writer.print(this.session.getUser() + "@spash:" + this.session.getWorkingDir() + "# ");
    }



}
