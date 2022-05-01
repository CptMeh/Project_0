package Test;

import Calculator.Operations_V1;
import org.junit.Test;

import javax.swing.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OperationsTestsV1 {

    @Test
    public void testEvaluateAdd() {
        ArrayList<String> q = new ArrayList<String>();
        q.add("3.0");
        q.add("+");
        q.add("3.0");

        Operations_V1 op = new Operations_V1(q);
        op.evaluate();
        assertEquals(6, op.getResult());
    }

    @Test
    public void testEvaluateSub() {
        ArrayList<String> q = new ArrayList<String>();
        q.add("3.0");
        q.add("+");
        q.add("-2.0");

        Operations_V1 op = new Operations_V1(q);
        op.evaluate();
        assertEquals(1, op.getResult());
    }

    @Test
    public void testEvaluateMult() {
        ArrayList<String> q = new ArrayList<String>();
        q.add("{");
        q.add("3.0");
        q.add("*");
        q.add("2.0");
        q.add("}");

        Operations_V1 op = new Operations_V1(q);
        op.evaluate();
        assertEquals(6.0, op.getResult());
    }

    @Test
    public void testEvaluateDiv() {
        ArrayList<String> q = new ArrayList<String>();
        q.add("{");
        q.add("4.0");
        q.add("/");
        q.add("2.0");
        q.add("}");

        Operations_V1 op = new Operations_V1(q);
        op.evaluate();
        assertEquals(2.0, op.getResult());
    }

    @Test
    public void testWriteCalc() {
        JTextArea fake = mock(JTextArea.class);
        Operations_V1 op = new Operations_V1(fake);
        op.parse("2.0");
        op.parse("+");
        op.parse("2.0");

        op.evaluate();
        assertEquals(4.0, op.getResult());
    }


    @Test
    public void test3Add3Add3Mult9() {
        JTextArea fake = mock(JTextArea.class);
        Operations_V1 op = new Operations_V1(fake);
        op.parse("3.0");
        op.parse("+");
        op.parse("3.0");
        op.parse("+");
        op.parse("3.0");
        op.parse("*");
        op.parse("9.0");

        op.evaluate();

        assertEquals(33.0, op.getResult());
    }

    @Test
    public void test3Add3Mult3Mult9() {
        JTextArea fake = mock(JTextArea.class);
        Operations_V1 op = new Operations_V1(fake);
        op.parse("3.0");
        op.parse("+");
        op.parse("3.0");
        op.parse("*");
        op.parse("3.0");
        op.parse("*");
        op.parse("9.0");

        op.evaluate();

        assertEquals(84.00, op.getResult());
    }

    @Test
    public void test3Add3Add3EvalMult9() {
        JTextArea fake = mock(JTextArea.class);
        Operations_V1 op = new Operations_V1(fake);
        op.parse("3.0");
        op.parse("+");
        op.parse("3.0");
        op.parse("+");
        op.parse("3.0");
        op.evaluate();

        op.parse("*");
        op.parse("9.0");
        op.evaluate();

        assertEquals(81.0, op.getResult());
    }

    @Test
    public void testAddWithAppendAndFloat() {
        JTextArea fake = mock(JTextArea.class);
        Operations_V1 op = new Operations_V1(fake);
        op.parse("3");
        op.parse("3");
        op.parse("+");
        op.parse("3");
        op.parse(".");
        op.parse("1");
        op.parse("+");
        op.parse("3");
        op.evaluate();

        assertEquals(39.1, op.getResult());
    }

    @Test
    public void testThatFuckingBooleanMultDivInPickOperation() { //fuck yeeea, I was just stoopiiiiif :D
        JTextArea fake = mock(JTextArea.class);
        Operations_V1 op = new Operations_V1(fake);
        op.setResult(9.0);
        op.parse("*");
        op.parse("9.0");
        op.evaluate();

        assertEquals(81.0, op.getResult());
    }

    @Test
    public void testDifferentMultGroups() {
        JTextArea fake = mock(JTextArea.class);
        Operations_V1 op = new Operations_V1(fake);
        op.parse("3");
        op.parse("*");
        op.parse("3");
        op.parse("+");
        op.parse("4");
        op.parse("*");
        op.parse("-");
        op.parse("4");
        op.evaluate();

        assertEquals(-7, op.getResult());
    }

    @Test
    public void testDifferentMultGroupsWithFloat() {
        JTextArea fake = mock(JTextArea.class);
        Operations_V1 op = new Operations_V1(fake);
        op.parse("3");
        op.parse("*");
        op.parse("3");
        op.parse("+");
        op.parse("4.66");
        op.parse("*");
        op.parse("-");
        op.parse("4");
        op.evaluate();

        assertEquals(-9.64, op.getResult());
    }

    @Test
    public void testSpecific() {
        JTextArea fake = mock(JTextArea.class);
        Operations_V1 op = new Operations_V1(fake);
        op.parse("2");
        op.parse("2");
        op.parse("2");
        op.parse("+");
        op.parse("1");
        op.parse("*");
        op.parse("8");
        op.parse("8");
        op.evaluate();

        assertEquals(310, op.getResult());
    }

    @Test
    public void testPunctuation() {
        JTextArea fake = mock(JTextArea.class);
        Operations_V1 op = new Operations_V1(fake);
        op.parse("2");
        op.parse(".");
        op.parse("1");
        op.parse("7");
        op.parse("8");
        op.parse("8");
        op.parse("+");
        op.parse("8");
        op.parse("8");
        op.evaluate();

        assertEquals(op.getResult(), 90.1788);
    }
}





























