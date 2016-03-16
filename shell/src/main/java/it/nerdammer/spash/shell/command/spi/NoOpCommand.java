package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.command.Command;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.SpashSession;

/**
 * A command that does not do anything.
 *
 * @author Nicola Ferraro
 */
public class NoOpCommand implements Command {

    public NoOpCommand(String commandStr) {}

    @Override
    public CommandResult execute(SpashSession session, CommandResult previousResult) {
        return CommandResult.success(this);
    }
}
