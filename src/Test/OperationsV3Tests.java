package Test;

import Calculator.Operations;
import Calculator.Operations_V3;
import org.junit.Test;

import javax.swing.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class OperationsV3Tests {

    private final boolean print = true;

    private Operations createNewOperation() {
        JTextArea fake = mock(JTextArea.class);
        Operations op = new Operations(fake);
        return op;
    }

    @Test
    public void testCheckCharacters() {
        StringBuilder sb = new StringBuilder();
        Operations_V3 o = new Operations_V3(sb);
        o.parse("3");
        o.parse("+");
        o.parse("3");
        o.evaluate();

        /*
        if (print) {
            System.out.println("CalcStr: " + o.getCalc());
            System.out.print("CalcArr: ");
            for (StringBuilder s : o.getCalcArr()) {
                System.out.print(s);
            }
            System.out.print("\n");
        }*/
    }

    @Test
    public void testEvaluateAdd() {
        StringBuilder q = new StringBuilder();
        q.append("3+3");

        Operations_V3 op = new Operations_V3(q);
        op.evaluate();
        assertEquals(6, op.getResult());
    }

    @Test
    public void testEvaluateAddWithOneBracket() {
        StringBuilder q = new StringBuilder("3+(3+3)");

        Operations_V3 op = new Operations_V3(q);
        op.evaluate();
        assertEquals(9, op.getResult());
    }

    @Test
    public void testEvaluateAddWithMultipleBrackets() {
        StringBuilder q = new StringBuilder("3+(3+(3+3))");

        Operations_V3 op = new Operations_V3(q);
        op.evaluate();
        assertEquals(12, op.getResult());
    }

    @Test
    public void testEvaluateAddAndMultWithMultipleBrackets() {
        StringBuilder q = new StringBuilder("3+(3*(3+3))");

        Operations_V3 op = new Operations_V3(q);
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

        Operations_V3 op = new Operations_V3();

        split = op.splitCalc(q);

        assertEquals(expected, split);
    }

    @Test
    public void testAssertions() {
        int i = 1;
        assertThrows(AssertionError.class, () -> {assert (i != 1);});
    }
}















