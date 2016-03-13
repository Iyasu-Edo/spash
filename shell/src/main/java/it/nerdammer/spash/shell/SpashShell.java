package it.nerdammer.spash.shell;

import it.nerdammer.spash.shell.command.Command;
import it.nerdammer.spash.shell.command.CommandFactory;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.session.SpashSession;
import jline.console.ConsoleReader;
import jline.console.completer.StringsCompleter;
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
            reader.setPrompt(getShellPrompt());
            reader.addCompleter(new StringsCompleter(CommandFactory.getInstance().getAvailableCommands().toArray(new String[] {})));
            writer = new PrintWriter(reader.getOutput());

            // output welcome banner on ssh session startup
            writer.println("****************************************************");
            writer.println("*                 Welcome to Spash                 *");
            writer.println("****************************************************");
            writer.println();
            writer.flush();

            String line;
            while ((line = reader.readLine()) != null) {
                handleUserInput(line.trim());

                // update the prompt
                reader.setPrompt(getShellPrompt());
            }

        } catch (InterruptedIOException | SpashExitException e) {
            // Ignore
        } catch (Exception e) {
            log.error("Error executing InAppShell...", e);
        } finally {
            callback.onExit(0);
        }
    }

    private void handleUserInput(String commandStr) throws InterruptedIOException {

        Command command = CommandFactory.getInstance().getCommand(commandStr);

        CommandResult result = command.execute(this.session);

        if(result.isSuccess()) {
            if(result.getContent()!=null) {
                result.getContent().mkString(writer);
            }
        } else {
            writer.println(result.getErrorMessage());
        }

        writer.flush();
    }

    private String getShellPrompt() {
        return this.session.getUser() + "@spash:" + this.session.getWorkingDir() + "# ";
    }

}
