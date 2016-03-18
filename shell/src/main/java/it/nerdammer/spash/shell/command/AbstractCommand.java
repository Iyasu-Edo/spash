package it.nerdammer.spash.shell.command;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides useful methods and constructor for concrete commands.
 *
 * @author Nicola Ferraro
 */
public abstract class AbstractCommand implements Command {

    private CommandTokenizer commandTokenizer;

    public AbstractCommand(String commandString) {
        this(commandString, Collections.emptyMap());
    }

    public AbstractCommand(String commandString, Map<String, Boolean> parametersValueInfo) {
        Set<String> parameters = parametersValueInfo.keySet();
        Set<String> valuedParameters = parametersValueInfo.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        this.commandTokenizer = new CommandTokenizer(commandString, valuedParameters);

        if(!parameters.containsAll(this.commandTokenizer.getOptions().keySet())) {
            Set<String> wrongParameters = new TreeSet<>(this.commandTokenizer.getOptions().keySet());
            wrongParameters.removeAll(parameters);
            throw new IllegalArgumentException("Unknown options: " + wrongParameters);
        }
    }


    /**
     * Returns the command associated to the command string.
     *
     * @return the command
     */
    public String getCommand() {
        return this.commandTokenizer.getCommand();
    }

    /**
     * Returns the option map associated to the command string.
     * Options not requiring a value contain null as value of the map.
     *
     * @return the option map
     */
    public Map<String, String> getOptions() {
        return this.commandTokenizer.getOptions();
    }

    /**
     * Returns the argument part of the command string (the one after the parameter set).
     *
     * @return the arguments
     */
    public List<String> getArguments() {
        return this.commandTokenizer.getArguments();
    }
}
