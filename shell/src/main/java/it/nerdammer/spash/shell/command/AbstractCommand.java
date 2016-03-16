package it.nerdammer.spash.shell.command;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 * Provides useful methods and constructor for concrete commands.
 *
 * @author Nicola Ferraro
 */
public abstract class AbstractCommand implements Command {

    private CommandTokenizer commandTokenizer;

    public AbstractCommand(String commandString) {
        this(commandString, Collections.emptySet(), Collections.emptySet());
    }

    public AbstractCommand(String commandString, Set<String> parameters, Set<String> valuedParameters) {
        this.commandTokenizer = new CommandTokenizer(commandString, valuedParameters);

        if(!parameters.containsAll(this.commandTokenizer.getOptions().keySet())) {
            Set<String> wrongParameters = new TreeSet<>(this.commandTokenizer.getOptions().keySet());
            wrongParameters.removeAll(parameters);
            throw new IllegalArgumentException("Unknown options: " + wrongParameters);
        }
    }

    /**
     * Returns the {@code CommandTokenizer} associated to the command string.
     *
     * @return the command tokenizer
     */
    protected CommandTokenizer getCommandTokenizer() {
        return commandTokenizer;
    }
}
