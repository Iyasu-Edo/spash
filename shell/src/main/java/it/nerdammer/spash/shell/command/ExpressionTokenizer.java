package it.nerdammer.spash.shell.command;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Tokenizes an expression to extract all command strings contained in it.
 *
 * @author Nicola Ferraro
 */
public class ExpressionTokenizer {

    private List<String> commandStrings;

    public ExpressionTokenizer(String arg) {
        if(arg==null) {
            throw new IllegalArgumentException("Null string");
        }
        if(containsUnquoted(arg, "||")) {
            throw new IllegalArgumentException("Unexpected sequence: '||'");
        }
        if(containsUnquoted(arg, ">>>")) {
            throw new IllegalArgumentException("Unexpected sequence: '>>>'");
        }

        List<String> piped = splitUnquoted(arg, "|");
        LinkedList<String> exprs = new LinkedList<>(piped.stream().map(String::trim).collect(Collectors.toList()));


        this.commandStrings = new ArrayList<>();
        while(!exprs.isEmpty()) {
            String expr = exprs.poll();
            Integer start = minPositive(indexOfUnquoted(expr, " "), indexOfUnquoted(expr, "\t"), indexOfUnquoted(expr, "\r"), indexOfUnquoted(expr, "\n"));
            if(start==null) {
                this.commandStrings.add(expr.trim());
                continue;
            }

            int idx = indexOfUnquoted(expr, ">", start);
            if(idx>0) {
                String first = expr.substring(0, idx).trim();
                this.commandStrings.add(first);
                exprs.addFirst(expr.substring(idx).trim());
            } else {
                this.commandStrings.add(expr.trim());
            }
        }
    }

    public List<String> getCommandStrings() {
        return commandStrings;
    }

    private Integer minPositive(Integer... args) {
        return min(Arrays.asList(args).stream().filter(e -> e >= 0).collect(Collectors.toList()));
    }

    private Integer min(List<Integer> args) {
        Integer min = null;
        for(int i=0; i<args.size(); i++) {
            min = min==null ? args.get(i) : Math.min(min, args.get(i));
        }
        return min;
    }

    protected List<String> splitUnquoted(String str, String sub) {
        List<String> part = new ArrayList<>();
        int pos = indexOfUnquoted(str, sub);
        if(pos >= 0) {
            String s = str.substring(0, pos);
            part.add(s);
            pos += sub.length();
            if(pos<str.length()) {
                String str2 = str.substring(pos);
                List<String> part2 = splitUnquoted(str2, sub);
                part.addAll(part2);
            }
        } else {
            part.add(str);
        }

        return part;
    }

    protected boolean containsUnquoted(String str, String sub) {
        return indexOfUnquoted(str, sub) >= 0;
    }

    protected int indexOfUnquoted(String str, String sub) {
        return indexOfUnquoted(str, sub, 0);
    }

    protected int indexOfUnquoted(String str, String sub, int startPos) {
        int start = startPos;
        while(start >= 0 && start < str.length()) {
            start = str.indexOf(sub, start);
            if(!isQuoted(str, start)) {
                return start;
            }
            start = start + 1;
        }
        return -1;
    }

    protected boolean isQuoted(String str, int pos) {
        int start = str.indexOf("\"");
        if(start < 0 || pos < start) {
            return false;
        }
        int end = str.indexOf("\"", start + 1);
        if(end < 0) {
            return false;
        }
        if(pos <= end) {
            return true;
        }

        return isQuoted(str.substring(end + 1), pos-end-1);
    }
}
