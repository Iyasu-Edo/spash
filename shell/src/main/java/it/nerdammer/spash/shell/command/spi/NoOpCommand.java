package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.common.SpashCollectionStreamAdapter;
import it.nerdammer.spash.shell.session.SpashSession;

import java.util.Collections;

/**
 * A command that does not do anything.
 *
 * @author Nicola Ferraro
 */
public class NoOpCommand extends AbstractCommand {

    public NoOpCommand(String commandStr) {
        super(commandStr);
    }

    @Override
    public CommandResult execute(SpashSession session) {
        CommandResult res = new CommandResult(this, true);
        res.setContent(new SpashCollectionStreamAdapter<>(Collections.<String>emptyList().stream()));
        return res;
    }
}
