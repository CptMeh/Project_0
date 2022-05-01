package Test;

import Calculator.Operations_V1;
import Calculator.Operations;
import org.junit.Test;

import javax.swing.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class OperationsTests {

    private final boolean print = true;

    private Operations_V1 createNewOperation() {
        JTextArea fake = mock(JTextArea.class);
        Operations_V1 op = new Operations_V1(fake);
        return op;
    }

    @Test
    public void testParseAndEvaluate() {
        StringBuilder sb = new StringBuilder();
        Operations o = new Operations(sb);
        o.parse("3");
        o.parse("+");
        o.parse("3");
        o.evaluate();

        assertEquals(6, o.getResult());
    }

    @Test
    public void testParseAndEvaluateMultiDigitNumbers() {
        StringBuilder sb = new StringBuilder();
        Operations o = new Operations(sb);
        o.parse("3");
        o.parse("+");
        o.parse("33");
        o.evaluate();

        assertEquals(36, o.getResult());
    }

    @Test
    public void testEvaluateAdd() {
        StringBuilder q = new StringBuilder();
        q.append("3+3");

        Operations op = new Operations(q);
        op.evaluate();
        assertEquals(6, op.getResult());
    }

    @Test
    public void testPunktVorStrich() {
        StringBuilder q = new StringBuilder();
        q.append("3+3*3");

        Operations op = new Operations(q);
        op.evaluate();
        assertEquals(12, op.getResult());
    }

    @Test
    public void testEvaluateAddWithOneBracket() {
        StringBuilder q = new StringBuilder("3+(3+3)");

        Operations op = new Operations(q);
        op.evaluate();
        assertEquals(9, op.getResult());
    }

    @Test
    public void testEvaluateAddWithMultipleBrackets() {
        StringBuilder q = new StringBuilder("3+(3+(3+3))");

        Operations op = new Operations(q);
        op.evaluate();
        assertEquals(12, op.getResult());
    }

    @Test
    public void testEvaluateAddAndMultWithMultipleBrackets() {
        StringBuilder q = new StringBuilder("3+(3*(3+3))");

        Operations op = new Operations(q);
        op.evaluate();
        assertEquals(21, op.getResult());
    }


    @Test
    public void testSplitCalc() {
        ArrayList<String> expected = new ArrayList<>();
        ArrayList<String> split;
        StringBuilder q = new StringBuilder();
        q.append("+12.34+");
        expected.add("+");
        expected.add("12.34");
        expected.add("+");

        Operations op = new Operations();

        split = op.splitCalc(q);

        assertEquals(expected, split);
    }

    @Test
    public void testAssertions() {
        int i = 1;
        assertThrows(AssertionError.class, () -> {assert (i != 1);});
    }
}















