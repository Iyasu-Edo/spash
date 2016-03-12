package it.nerdammer.spash.shell;

import com.sun.tools.javac.comp.Env;
import it.nerdammer.spash.shell.auth.User;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;

import java.io.*;

/**
 * A java shell that emulates bash.
 *
 * @author Nicola Ferraro
 */
public class SpashShell implements Command, Runnable {

    private User user;

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

        this.user = new User(environment.getEnv().get(Environment.ENV_USER));

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

            final PrintWriter writer = new PrintWriter(outputStream, true);
            writer.print("Welcome to Spash 0.1 (Spark 1.6.0, Hadoop 2.6.0)\r\n");
            writer.print("\r\n");
            writeHeading(writer);
            writer.flush();


            StringBuilder builder = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(this.inputStream);
            int read;
            do {
                read = reader.read();
                switch (read) {
                    case 10:
                    case 13:
                        builder.delete(0, builder.length());
                        writer.append("\r\n");
                        writeHeading(writer);

                        break;
                    case 3:
                        this.destroy();

                        break;
                    default:
                        char ch = (char) read;
                        builder.append(ch);
                        writer.append(ch);
                }

                writer.flush();
            } while (read >= 0);

        } catch(InterruptedIOException ie) {
            System.out.println("Closed");
        } catch (Exception e) {
            System.out.println("Closed abnormally");
        }
    }

    private void writeHeading(PrintWriter writer) {
        writer.print(this.user.getUsername() + "@spash:/# ");
    }



}
