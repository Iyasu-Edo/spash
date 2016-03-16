package it.nerdammer.spash.shell.command;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tokenizes a command string to allow getting all the parts.
 *
 * @author Nicola Ferraro
 */
public class CommandTokenizer {

    private static Pattern regex = Pattern.compile("\"[^\"]+\"|[^\\s]+");

    private Set<String> valuedParameters;

    private String command;

    private Map<String, String> options;

    private List<String> arguments;

    public CommandTokenizer(String arg) {
        this(arg, Collections.emptySet());
    }

    public CommandTokenizer(String arg, Set<String> valuedParameters) {
        if(arg==null || arg.trim().length()==0) {
            throw new IllegalArgumentException("Empty string: '" + arg + "'");
        }
        if(valuedParameters==null) {
            throw new IllegalArgumentException("valued parameters cannot be null, use an empty set");
        }

        this.valuedParameters = valuedParameters;


        List<String> components = new ArrayList<>();
        Matcher m = regex.matcher(arg);
        while(m.find()) {
            String next = m.group();
            components.add(next);
        }

        if(components.size()==0) {
            throw new IllegalArgumentException("No command found");
        }

        this.command = components.get(0);

        this.options = new TreeMap<>();
        int i=1;
        for(; i<components.size(); i++) {
            String c = components.get(i);
            if(!c.startsWith("-")) {
                break;
            }

            if(c.startsWith("--")) {
                // double dash
                String param = c.substring(2);
                String value = null;
                if(param.trim().length()==0) {
                    throw new IllegalArgumentException("No param defined after '--'");
                } else if(this.options.containsKey(param)) {
                    throw new IllegalArgumentException("Parameter '" + param + "' is defined twice");
                }

                boolean needsValue = this.valuedParameters.contains(param);
                if(needsValue && i==components.size()-1) {
                    throw new IllegalArgumentException("Missing value for parameter '" + param + "'");
                } else if(needsValue) {
                    value = components.get(++i);
                }

                this.options.put(param, value);
            } else {
                // single dash
                String param = c.substring(1);
                if(param.trim().length()==0) {
                    throw new IllegalArgumentException("No param defined after '-'");
                }

                if(param.length()>1) {
                    // multiparam
                    for(char p : param.toCharArray()) {
                        String pp = p + "";
                        if(this.options.containsKey(pp)) {
                            throw new IllegalArgumentException("Parameter '" + pp + "' is defined twice");
                        }
                        if(this.valuedParameters.contains(pp)) {
                            throw new IllegalArgumentException("Missing value for parameter '" + pp + "'");
                        }
                        this.options.put(pp, null);
                    }
                } else {
                    // single param
                    if(this.options.containsKey(param)) {
                        throw new IllegalArgumentException("Parameter '" + param + "' is defined twice");
                    }

                    String value = null;

                    boolean needsValue = this.valuedParameters.contains(param);
                    if(needsValue && i==components.size()-1) {
                        throw new IllegalArgumentException("Missing value for parameter '" + param + "'");
                    } else if(needsValue) {
                        value = components.get(++i);
                    }

                    this.options.put(param, value);
                }

            }
        }

        this.arguments = new ArrayList<>();
        for(; i<components.size(); i++) {
            this.arguments.add(unquoted(components.get(i)));
        }
    }

    /**
     * Returns the command associated to the command string.
     *
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the option map associated to the command string.
     * Options not requiring a value contain null as value of the map.
     *
     * @return the option map
     */
    public Map<String, String> getOptions() {
        return this.options;
    }

    /**
     * Returns the argument part of the command string (the one after the parameter set).
     *
     * @return the arguments
     */
    public List<String> getArguments() {
        return arguments;
    }

    private String unquoted(String s) {
        if(s.startsWith("\"") && s.endsWith("\"") && s.length()>=2) {
            s = s.substring(1, s.length()-1);
        }
        return s;
    }

}
