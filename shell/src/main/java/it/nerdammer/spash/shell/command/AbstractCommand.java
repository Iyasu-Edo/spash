package it.nerdammer.spash.shell.command;

/**
 * Provides useful methods and constructor for concrete commands.
 *
 * @author Nicola Ferraro
 */
public abstract class AbstractCommand implements Command {

    protected String commandString;

    public AbstractCommand(String commandString) {
        this.commandString = commandString;
    }

    /**
     * Retrieves the first argument passed to this command.
     *
     * @return the first argument passed to the shell
     */
    protected String getFirstArgument() {
        String[] parts = commandString.split(" ");
        if(parts.length>1) {
            return parts[1];
        }
        return null;
    }

}
