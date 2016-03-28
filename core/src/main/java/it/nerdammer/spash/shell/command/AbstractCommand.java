package it.nerdammer.spash.shell.command;

import java.util.*;

/**
 * Provides useful methods and constructor for concrete commands.
 *
 * @author Nicola Ferraro
 */
public abstract class AbstractCommand implements Command {

    private CommandTokenizer commandTokenizer;

    public AbstractCommand(String commandString) {
        this(commandString, Collections.<String, Boolean>emptyMap());
    }

    public AbstractCommand(String commandString, Map<String, Boolean> parametersValueInfo) {
        Set<String> parameters = parametersValueInfo.keySet();
        Set<String> valuedParameters = new TreeSet<>();
        for(Map.Entry<String, Boolean> e : parametersValueInfo.entrySet()) {
            if(e.getValue().booleanValue()) {
                valuedParameters.add(e.getKey());
            }
        }

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
