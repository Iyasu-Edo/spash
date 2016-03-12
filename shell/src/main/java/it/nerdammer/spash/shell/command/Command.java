package it.nerdammer.spash.shell.command;

import it.nerdammer.spash.shell.session.SpashSession;

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
     * @return the associated {@code CommandResult}
     */
    CommandResult execute(SpashSession session);

}
