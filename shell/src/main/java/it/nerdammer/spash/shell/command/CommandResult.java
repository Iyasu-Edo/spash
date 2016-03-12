package it.nerdammer.spash.shell.command;

import it.nerdammer.spash.shell.common.SpashCollection;

/**
 * Defines the result of the execution of a command.
 *
 * @author Nicola Ferraro
 */
public class CommandResult {

    /**
     * The executed command.
     */
    private Command command;

    /**
     * Indicates whether the command completed successfully.
     */
    private boolean success;

    /**
     * An error message explaining the problem when the command failed to execute.
     */
    private String errorMessage;

    /**
     * The result of the command.
     */
    private SpashCollection<String> content;

    public CommandResult(Command command, boolean success) {
        this.command = command;
        this.success = success;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public SpashCollection<String> getContent() {
        return content;
    }

    public void setContent(SpashCollection<String> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommandResult{" +
                "command=" + command +
                ", success=" + success +
                '}';
    }

}
