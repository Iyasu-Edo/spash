package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.command.ExecutionContext;
import it.nerdammer.spash.shell.common.SpashCollection;
import it.nerdammer.spash.shell.common.SpashCollectionStreamAdapter;
import it.nerdammer.spash.shell.SpashSession;

import java.util.Collections;
import java.util.List;

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
    public CommandResult execute(ExecutionContext ctx) {

        List<String> args = this.getArguments();
        if(args.size()>0) {
            return CommandResult.error(this, "Unexpected arguments: " + args);
        }

        SpashCollection<String> content = new SpashCollectionStreamAdapter<>(Collections.singleton(ctx.getSession().getWorkingDir()).stream());
        return CommandResult.success(this, content);
    }
}
