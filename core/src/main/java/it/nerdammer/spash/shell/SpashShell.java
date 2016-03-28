package it.nerdammer.spash.shell;

import it.nerdammer.spash.shell.command.*;
import jline.console.ConsoleReader;
import jline.console.UserInterruptException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.LinkedList;

/**
 * A java shell that emulates bash.
 *
 * @author Nicola Ferraro
 */
public class SpashShell implements org.apache.sshd.server.Command, Runnable {

    private static final Logger log = LoggerFactory.getLogger(SpashShell.class);

    public static final boolean IS_MAC_OSX = System.getProperty("os.name").startsWith("Mac OS X");

    private InputStream in;
    private OutputStream out;
    private OutputStream err;
    private ExitCallback callback;
    private Environment environment;
    private Thread thread;
    private ConsoleReader reader;
    private PrintWriter writer;

    private SpashSession session;

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }

    public OutputStream getErr() {
        return err;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setInputStream(InputStream in) {
        this.in = in;
    }

    public void setOutputStream(OutputStream out) {
        this.out = out;
    }

    public void setErrorStream(OutputStream err) {
        this.err = err;
    }

    public void setExitCallback(ExitCallback callback) {
        this.callback = callback;
    }

    public void start(Environment env) throws IOException {
        this.environment = env;
        this.session = new SpashSession(environment.getEnv().get(Environment.ENV_USER));

        this.thread = new Thread(this);
        this.thread.start();
    }

    public void destroy() {
        if (reader != null)
            reader.shutdown();
        thread.interrupt();
    }

    @Override
    public void run() {
        try {

            reader = new ConsoleReader(in, new FilterOutputStream(out) {
                @Override
                public void write(final int i) throws IOException {
                    super.write(i);

                    // workaround for MacOSX!! reset line after CR..
                    if (IS_MAC_OSX && i == ConsoleReader.CR.toCharArray()[0]) {
                        super.write(ConsoleReader.RESET_LINE);
                    }
                }
            });

            // enable ctrl+c
            reader.setHandleUserInterrupt(true);

            // Set the prompt
            reader.setPrompt(getShellPrompt());

            // Set the completer
            reader.addCompleter(new SpashCommandCompleter(this.session));

            writer = new PrintWriter(reader.getOutput());

            // output welcome banner on ssh session startup
            writer.println("****************************************************");
            writer.println("*                 Welcome to Spash                 *");
            writer.println("****************************************************");
            writer.println();
            writer.flush();

            String line;
            do {
                try {
                    line = reader.readLine();
                }
                catch(UserInterruptException e) {
                    log.debug("ctrl+c");
                    line = "";
                    continue;
                }

                if(line!=null) {
                    handleUserInput(line.trim());

                    // update the prompt
                    reader.setPrompt(getShellPrompt());
                }

            } while(line!=null);


        } catch (InterruptedIOException | SpashExitException e) {
            // Ignore
        } catch (Throwable t) {
            writer.println("-spash: Unexpected error while executing the command. The shell will be closed.");
            writer.println(ExceptionUtils.getStackTrace(t));
            writer.flush();
            log.error("Error executing InAppShell...", t);
        } finally {
            callback.onExit(0);
        }
    }


    private void handleUserInput(String exprStr) throws InterruptedIOException {

        try {
            ExpressionTokenizer tok = new ExpressionTokenizer(exprStr);

            ExecutionContext ctx = new ExecutionContext(this.session, null);

            LinkedList<String> commands = new LinkedList<>(tok.getCommandStrings());

            while(!commands.isEmpty()) {
                String commandStr = commands.poll();

                Command command = CommandFactory.getInstance().getCommand(commandStr);

                CommandResult result = command.execute(ctx);

                if(result.isSuccess() && !commands.isEmpty()) {
                    ctx.setPreviousCommandResult(result);
                    continue;
                }

                if (result.isSuccess()) {
                    if (result.getContent() != null) {
                        result.getContent().mkString(writer);
                    }
                } else {
                    writer.print("-spash: ");
                    String[] parts = commandStr.split(" ");
                    for (String p : parts) {
                        writer.print(p + ": ");
                    }
                    writer.println(result.getErrorMessage());
                }
            }
        } catch(IllegalArgumentException e) {
            writer.print("-spash: ");
            writer.println(e.getMessage());
        }

        writer.flush();
    }

    private String getShellPrompt() {
        return this.session.getUser() + "@spash:" + this.session.getWorkingDir() + "# ";
    }

}
