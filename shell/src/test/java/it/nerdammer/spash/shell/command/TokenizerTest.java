package it.nerdammer.spash.shell.command;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Nicola Ferraro
 */
public class TokenizerTest {

    @Test
    public void testNormalString() {
        CommandTokenizer t = new CommandTokenizer("ls -l");

        assertEquals("ls", t.getCommand());
        assertEquals(asMap("l", null), t.getOptions());
        assertEquals(Collections.emptyList(), t.getArguments());
    }

    @Test
    public void testQuotedString() {
        CommandTokenizer t = new CommandTokenizer("ls -l \"a\"");

        assertEquals("ls", t.getCommand());
        assertEquals(asMap("l", null), t.getOptions());
        assertEquals(Arrays.asList("a"), t.getArguments());
    }

    @Test
    public void testQuoteOnFirstSegment() {
        CommandTokenizer t = new CommandTokenizer("\"ls\" -l\" a");

        assertEquals("\"ls\"", t.getCommand());
        assertEquals(asMap("l", null,
                            "\"", null), t.getOptions());
        assertEquals(Arrays.asList("a"), t.getArguments());
    }

    @Test
    public void testSpaces() {
        CommandTokenizer t = new CommandTokenizer("ls   a");

        assertEquals("ls", t.getCommand());
        assertEquals(Collections.emptyMap(), t.getOptions());
        assertEquals(Arrays.asList("a"), t.getArguments());
    }

    @Test
    public void testSpacesInsideQuotes() {
        CommandTokenizer t = new CommandTokenizer("ls   a  \"   \"  \"  b  \"");

        assertEquals("ls", t.getCommand());
        assertEquals(Collections.emptyMap(), t.getOptions());
        assertEquals(Arrays.asList("a", "   ", "  b  "), t.getArguments());
    }

    @Test
    public void testSpecialSpaces() {
        CommandTokenizer t = new CommandTokenizer("cat \r \n \t a bbb \n");

        assertEquals("cat", t.getCommand());
        assertEquals(Collections.emptyMap(), t.getOptions());
        assertEquals(Arrays.asList("a", "bbb"), t.getArguments());
    }

    @Test
    public void testParameterlessValue() {
        CommandTokenizer t = new CommandTokenizer("cat -t hello world");

        assertEquals("cat", t.getCommand());
        assertEquals(asMap("t", null), t.getOptions());
        assertEquals(Arrays.asList("hello", "world"), t.getArguments());
    }

    @Test
    public void testParameterlessMultiparamValue() {
        CommandTokenizer t = new CommandTokenizer("cat -tr hello world");

        assertEquals("cat", t.getCommand());
        assertEquals(asMap("t", null,
                            "r", null), t.getOptions());
        assertEquals(Arrays.asList("hello", "world"), t.getArguments());
    }

    @Test
    public void testParameterValue() {
        CommandTokenizer t = new CommandTokenizer("cat -t hello world", Collections.singleton("t"));

        assertEquals("cat", t.getCommand());
        assertEquals(asMap("t", "hello"), t.getOptions());
        assertEquals(Arrays.asList("world"), t.getArguments());
    }

    @Test
    public void testMultiParameterValue() {
        CommandTokenizer t = new CommandTokenizer("cat -t hello -r  world  yes", new TreeSet<>(Arrays.asList("r", "t")));

        assertEquals("cat", t.getCommand());
        assertEquals(asMap("t", "hello",
                            "r", "world"), t.getOptions());
        assertEquals(Arrays.asList("yes"), t.getArguments());
    }

    @Test
    public void testTextParameter() {
        CommandTokenizer t = new CommandTokenizer("cat --trello world  yes");

        assertEquals("cat", t.getCommand());
        assertEquals(asMap("trello", null), t.getOptions());
        assertEquals(Arrays.asList("world", "yes"), t.getArguments());
    }

    @Test
    public void testWrongOptionAsParameter() {
        CommandTokenizer t = new CommandTokenizer("cat --trello world yes -p ciao");

        assertEquals("cat", t.getCommand());
        assertEquals(asMap("trello", null), t.getOptions());
        assertEquals(Arrays.asList("world", "yes", "-p", "ciao"), t.getArguments());
    }

    @Test
    public void testTextParameterValue() {
        CommandTokenizer t = new CommandTokenizer("cat --trello  world  yes", new TreeSet<>(Arrays.asList("trello")));

        assertEquals("cat", t.getCommand());
        assertEquals(asMap("trello", "world"), t.getOptions());
        assertEquals(Arrays.asList("yes"), t.getArguments());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCommand() {
        new CommandTokenizer("cat --trello", new TreeSet<>(Arrays.asList("trello")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSingleCommand() {
        new CommandTokenizer("cat -t", new TreeSet<>(Arrays.asList("t")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSingleCommandRequiringValue() {
        new CommandTokenizer("cat -te pi", new TreeSet<>(Arrays.asList("t")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingleParamDefinedTwice() {
        new CommandTokenizer("cat -te -t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingleParamDefinedTwice2() {
        new CommandTokenizer("cat -t -t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingleParamDefinedTwice3() {
        new CommandTokenizer("cat -tt");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSingleParamDefinedTwice4() {
        new CommandTokenizer("cat -t -e -t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextParamDefinedTwice() {
        new CommandTokenizer("cat --te --te");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextParamDefinedTwice2() {
        new CommandTokenizer("cat --t --t");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTextParamDefinedTwice3() {
        new CommandTokenizer("cat --help --help");
    }

    private Map<String, String> asMap(String... values) {
        Map<String, String> map = new TreeMap<>();
        for(int i=0; i+1<=values.length; i+=2) {
            map.put(values[i], values[i+1]);
        }
        return map;
    }

}
