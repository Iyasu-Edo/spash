package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.SpashExitException;
import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.SpashSession;

import java.util.List;

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
    public CommandResult execute(SpashSession session, CommandResult previousResult) {

        List<String> args = this.getCommandTokenizer().getArguments();
        if(args.size()>0) {
            return CommandResult.error(this, "Unexpected arguments: " + args);
        }

        throw new SpashExitException();
    }
}
