package it.nerdammer.spash.shell.command;

import it.nerdammer.spash.shell.SpashSession;

/**
 * The context associated with the execution of a {@code Command}.
 *
 * @author Nicola Ferraro
 */
public class ExecutionContext {

    private SpashSession session;

    private CommandResult previousCommandResult;

    public ExecutionContext() {
    }

    public ExecutionContext(SpashSession session, CommandResult previousCommandResult) {
        this.session = session;
        this.previousCommandResult = previousCommandResult;
    }

    public SpashSession getSession() {
        return session;
    }

    public void setSession(SpashSession session) {
        this.session = session;
    }

    public CommandResult getPreviousCommandResult() {
        return previousCommandResult;
    }

    public void setPreviousCommandResult(CommandResult previousCommandResult) {
        this.previousCommandResult = previousCommandResult;
    }
}
