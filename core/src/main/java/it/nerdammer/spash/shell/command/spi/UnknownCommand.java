package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.command.Command;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.SpashSession;
import it.nerdammer.spash.shell.command.ExecutionContext;

/**
 * @author Nicola Ferraro
 */
public class UnknownCommand implements Command {

    public UnknownCommand(String commandString) {
    }

    @Override
    public CommandResult execute(ExecutionContext ctx) {
        return CommandResult.error(this, "command not found");
    }
}
