package it.nerdammer.spash.shell.command.spi;

import it.nerdammer.spash.shell.command.AbstractCommand;
import it.nerdammer.spash.shell.command.CommandResult;
import it.nerdammer.spash.shell.session.SpashSession;
import org.apache.commons.math3.analysis.function.Abs;

/**
 * @author Nicola Ferraro
 */
public class UnknownCommand extends AbstractCommand {

    public UnknownCommand(String commandString) {
        super(commandString);
    }

    @Override
    public CommandResult execute(SpashSession session) {
        CommandResult res = new CommandResult(this, false);
        res.setErrorMessage("-spash: " + commandString + ": command not found");
        return res;
    }
}
