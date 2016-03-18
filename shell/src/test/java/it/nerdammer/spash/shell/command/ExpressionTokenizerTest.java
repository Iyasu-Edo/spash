package it.nerdammer.spash.shell.command;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Nicola Ferraro
 */
public class ExpressionTokenizerTest {

    @Test
    public void testQuoted1() {
        String str = "01234\"678\"0";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertFalse(t.isQuoted(str, 0));
        assertFalse(t.isQuoted(str, 1));
        assertFalse(t.isQuoted(str, 4));

        assertTrue(t.isQuoted(str, 5));
        assertTrue(t.isQuoted(str, 6));
        assertTrue(t.isQuoted(str, 7));
        assertTrue(t.isQuoted(str, 8));
        assertTrue(t.isQuoted(str, 9));

        assertFalse(t.isQuoted(str, 10));
    }

    @Test
    public void testQuoted2() {
        String str = "01234\"678\"0";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertFalse(t.isQuoted(str, 11));
        assertFalse(t.isQuoted(str, 15));
    }

    @Test
    public void testQuoted3() {
        String str = "01234\"67890";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertFalse(t.isQuoted(str, 11));
        assertFalse(t.isQuoted(str, 15));
    }

    @Test
    public void testQuoted4() {
        String str = "01234\"678\"01\"";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertTrue(t.isQuoted(str, 6));
        assertTrue(t.isQuoted(str, 7));
        assertTrue(t.isQuoted(str, 8));
        assertTrue(t.isQuoted(str, 9));

        assertFalse(t.isQuoted(str, 10));
        assertFalse(t.isQuoted(str, 11));
        assertFalse(t.isQuoted(str, 12));
        assertFalse(t.isQuoted(str, 13));
        assertFalse(t.isQuoted(str, 14));
    }

    @Test
    public void testContainsUnquoted1() {
        String str = "ls -ltr >> file1.txt";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(8, t.indexOfUnquoted(str, ">>"));
    }

    @Test
    public void testContainsUnquoted2() {
        String str = "ls -ltr \">>\" file1.txt";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(-1, t.indexOfUnquoted(str, ">>"));
    }

    @Test
    public void testContainsUnquoted3() {
        String str = "ls \"-ltr >>\" file1.txt";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(-1, t.indexOfUnquoted(str, ">>"));
    }

    @Test
    public void testContainsUnquoted4() {
        String str = "ls \"-ltr >>\" > file1.txt";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(13, t.indexOfUnquoted(str, ">"));
    }

    @Test
    public void testContainsUnquoted5() {
        String str = "ls \"-ltr >>\" > > file1.txt";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(13, t.indexOfUnquoted(str, ">"));
        assertEquals(15, t.indexOfUnquoted(str, ">", 14));
    }

    @Test
    public void testContainsUnquoted6() {
        String str = "ls \"-ltr >\">\" > >> file1.txt";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(11, t.indexOfUnquoted(str, ">"));
        assertEquals(14, t.indexOfUnquoted(str, ">", 12));
        assertEquals(16, t.indexOfUnquoted(str, ">", 15));
        assertEquals(17, t.indexOfUnquoted(str, ">", 17));
    }

    @Test
    public void testSplitUnquoted1() {
        String str = "ls -ltr | grep -v hello";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(Arrays.asList("ls -ltr ", " grep -v hello"), t.splitUnquoted(str, "|"));
    }

    @Test
    public void testSplitUnquoted2() {
        String str = "ls -ltr \"|\" | grep -v hello";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(Arrays.asList("ls -ltr \"|\" ", " grep -v hello"), t.splitUnquoted(str, "|"));
    }

    @Test
    public void testTokenizer1() {
        String str = "ls -ltr \"|\" | grep -v hello";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(Arrays.asList("ls -ltr \"|\"", "grep -v hello"), t.getCommandStrings());
    }

    @Test
    public void testTokenizer2() {
        String str = "ls -ltr \"|\" \">>>\" > 2 | grep -v hello";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(Arrays.asList("ls -ltr \"|\" \">>>\"", "> 2", "grep -v hello"), t.getCommandStrings());
    }

    @Test
    public void testTokenizer3() {
        String str = "ls -ltr \"|\" \">>>\" >> 2 | grep -v hello | > asd";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(Arrays.asList("ls -ltr \"|\" \">>>\"", ">> 2", "grep -v hello", "> asd"), t.getCommandStrings());
    }

    @Test
    public void testTokenizer4() {
        String str = "echo hello > file";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(Arrays.asList("echo hello", "> file"), t.getCommandStrings());
    }

    @Test
    public void testTokenizer5() {
        String str = " > file";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(Arrays.asList("> file"), t.getCommandStrings());
    }

    @Test
    public void testTokenizer6() {
        String str = "";
        ExpressionTokenizer t = new ExpressionTokenizer(str);

        assertEquals(Arrays.asList(""), t.getCommandStrings());
    }

}
