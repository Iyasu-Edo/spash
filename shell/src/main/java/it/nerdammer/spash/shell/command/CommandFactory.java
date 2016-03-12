package it.nerdammer.spash.shell.command;

import it.nerdammer.spash.shell.command.spi.LsCommand;
import it.nerdammer.spash.shell.command.spi.NoOpCommand;
import it.nerdammer.spash.shell.command.spi.PwdCommand;
import it.nerdammer.spash.shell.command.spi.UnknownCommand;

/**
 * Defines all the commands available on Spash.
 *
 * @author Nicola Ferraro
 */
public class CommandFactory {

    private static final CommandFactory INSTANCE = new CommandFactory();

    private CommandFactory() {}

    public static CommandFactory getInstance() {
        return INSTANCE;
    }

    public Command getCommand(String commandStr) {

        if(commandStr.trim().equals("")) {
            return new NoOpCommand(commandStr);
        } else if(commandStr.equals("ls")) {
            return new LsCommand(commandStr);
        } else if(commandStr.equals("pwd")) {
            return new PwdCommand(commandStr);
        }

        return new UnknownCommand(commandStr);
    }

}
