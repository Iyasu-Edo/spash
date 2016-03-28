package it.nerdammer.spash.shell.command;

import it.nerdammer.spash.shell.SpashSession;

/**
 * An abstract Spash command.
 *
 * @author Nicola Ferraro
 */
public interface Command {

    /**
     * Executes the command action.
     *
     * @param ctx the execution context
     * @return the associated {@code CommandResult}
     */
    CommandResult execute(ExecutionContext ctx);

}
