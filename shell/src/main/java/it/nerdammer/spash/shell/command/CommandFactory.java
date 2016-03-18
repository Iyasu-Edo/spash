package it.nerdammer.spash.shell.command;

import it.nerdammer.spash.shell.command.spi.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Defines all the commands available on Spash.
 *
 * @author Nicola Ferraro
 */
public class CommandFactory {

    private static final CommandFactory INSTANCE = new CommandFactory();

    private Map<String, Class<? extends Command>> commands;

    private CommandFactory() {
        this.commands = new TreeMap<>();
        commands.put("cat", CatCommand.class);
        commands.put("cd", CdCommand.class);
        commands.put("echo", EchoCommand.class);
        commands.put("exit", ExitCommand.class);
        commands.put("grep", GrepCommand.class);
        commands.put("head", HeadCommand.class);
        commands.put("ls", LsCommand.class);
        commands.put("mkdir", MkDirCommand.class);
        commands.put("pwd", PwdCommand.class);
        commands.put("rmdir", RmDirCommand.class);
    }

    public static CommandFactory getInstance() {
        return INSTANCE;
    }

    public Set<String> getAvailableCommands() {
        return this.commands.keySet();
    }

    public Command getCommand(String commandStr) {

        if(commandStr.trim().equals("")) {
            return new NoOpCommand(commandStr);
        } else {
            String[] args = commandStr.split(" "); // TODO parse

            if(commands.containsKey(args[0])) {
                try {
                    Class<? extends Command> commandClass = commands.get(args[0]);
                    Command c = commandClass.getConstructor(String.class).newInstance(commandStr);
                    return c;
                } catch(InvocationTargetException e) {
                    Throwable t = e.getCause();
                    if(t instanceof RuntimeException) {
                        throw (RuntimeException)t;
                    } else if(t instanceof Error) {
                        throw (Error)t;
                    } else {
                        throw new IllegalStateException(t);
                    }
                } catch(RuntimeException e) {
                    throw e;
                } catch(Exception e) {
                    throw new IllegalStateException("Unable to create command '" + commandStr + "'", e);
                }
            }
        }

        return new UnknownCommand(commandStr);
    }

}
