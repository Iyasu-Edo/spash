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
     * @param session the current user sesssion
     * @param previousResult the result of the previous command run by the shell
     * @return the associated {@code CommandResult}
     */
    CommandResult execute(SpashSession session, CommandResult previousResult);

}
