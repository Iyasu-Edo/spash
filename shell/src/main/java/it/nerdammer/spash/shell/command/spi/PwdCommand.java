package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.common.SpashCollectionStreamAdapter;
import it.nerdammer.spash.shell.session.SpashSession;

import java.util.Collections;
import java.util.stream.Stream;

/**
 * A command that returns the current directory.
 *
 * @author Nicola Ferraro
 */
public class PwdCommand extends AbstractCommand {

    public PwdCommand(String commandString) {
        super(commandString);
    }

    @Override
    public CommandResult execute(SpashSession session) {
        CommandResult res = new CommandResult(this, true);
        res.setContent(new SpashCollectionStreamAdapter<>(Collections.singleton(session.getWorkingDir()).stream()));
        return res;
    }
}
