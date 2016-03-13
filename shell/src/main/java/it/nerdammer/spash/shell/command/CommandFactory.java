package it.nerdammer.spash.shell.command;

import it.nerdammer.spash.shell.command.spi.*;

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
        commands.put("cd", CdCommand.class);
        commands.put("exit", ExitCommand.class);
        commands.put("ls", LsCommand.class);
        commands.put("pwd", PwdCommand.class);
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
                } catch(Exception e) {
                    throw new IllegalStateException("Unable to create command '" + commandStr + "'", e);
                }
            }
        }

        return new UnknownCommand(commandStr);
    }

}
