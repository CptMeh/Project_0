package Calculator;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

/**
 * Responsible for creating a GUI of a Calculator.
 * The JFrame contains an editable input field and number and symbol buttons.
 */
public class Calculator extends JFrame {
    private final Operations op;
    private PrintStream output;

    public static void main(String[] args) {
        var textArea = new JTextArea(3, 25);
        var c = new Calculator(textArea, new PrintStream(new TextOutputStream(textArea)));
    }

    public Calculator(JTextArea textArea, PrintStream output) {
        ButtonGrid buttonGrid;
        setUp(textArea);
        this.output = output;
        this.op = new Operations(textArea);
        System.setOut(output);
        buttonGrid = new ButtonGrid(op, this.output);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        add(buttonGrid, "South");
        setSize(350, 400);
    }

    /**
     * Sets up the Clear Button and the TextArea, then adds them to a Panel for formatting,
     * and lastly adds it to the main JFrame.
     */
    private void setUp(JTextArea textArea) {
        var panel = new JPanel();
        var clear = new JButton("CLEAR");

        setUpClearButton(clear);
        setUpTextArea(textArea);

        panel.add(clear, "West");
        panel.add(textArea, "East");

        add(panel,"North");
    }

    /**
     * Sets up the clear button which clears the calculation when prressed.
     */
    private void setUpClearButton(JButton clear) {
        clear.setPreferredSize(new Dimension(75, 47));
        clear.setBackground(Color.GRAY);
        clear.addActionListener(e -> op.hardClear());
    }

    /**
     * Sets up the TextArea in which the calculation is typed in by the user.
     */
    private void setUpTextArea(JTextArea textArea) {
        textArea.setSize(200, 50);
        textArea.setLayout(new BorderLayout());
        textArea.setEditable(false);
    }

    public void setOutput(PrintStream output) {
        this.output = output;
        System.setOut(output);
    }

    public PrintStream getOutput() {
        return output;
    }
}
