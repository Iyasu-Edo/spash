package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.SpashExitException;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.common.SpashCollectionStreamAdapter;
import it.nerdammer.spash.shell.session.SpashSession;

import java.io.InterruptedIOException;
import java.util.Collections;

/**
 * A command that exits the shell.
 *
 * @author Nicola Ferraro
 */
public class ExitCommand extends AbstractCommand {

    public ExitCommand(String commandString) {
        super(commandString);
    }

    @Override
    public CommandResult execute(SpashSession session) {
        throw new SpashExitException();
    }
}
