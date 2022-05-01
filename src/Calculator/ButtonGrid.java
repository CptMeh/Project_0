package Calculator;

import javax.swing.*;
import java.awt.*;

import java.io.PrintStream;

public class ButtonGrid extends JPanel {
    private static final int COLS = 4;
    private static final int ROWS = 5;
    private final JButton[][] buttonArray = new JButton[ROWS][COLS]; // COLS: | ; ROWS : _
    private final Operations op;
    private final PrintStream output;

    public ButtonGrid(Operations op, PrintStream output) {
        this.output = output;
        this.op = op;
        setLayout(new GridLayout(ROWS, COLS));
        setSize(400, 400);
        setUpButtons();
    }

    private void setUpButtons() {
        int number = 1;

        buttonArray[0][0] = setUpClearButton();
        buttonArray[0][1] = new Button("(", op, output);
        buttonArray[0][2] = new Button(")", op, output);
        buttonArray[0][3] = new Button("÷", op, output);

        buttonArray[1][3] = new Button("*", op, output);
        buttonArray[2][3] = new Button("-", op, output);
        buttonArray[3][3] = new Button("+", op, output);
        buttonArray[4][3] = new Button("=", op, output);

        buttonArray[4][0] = new Button(".", op, output);
        buttonArray[4][1] = new Button("0", op, output);
        buttonArray[4][2] = new Button("", op, output);

        buttonArray[1][0] = new Button("1", op, output);
        buttonArray[1][1] = new Button("2", op, output);
        buttonArray[1][2] = new Button("3", op, output);

        buttonArray[2][0] = new Button("4", op, output);
        buttonArray[2][1] = new Button("5", op, output);
        buttonArray[2][2] = new Button("6", op, output);

        buttonArray[3][0] = new Button("7", op, output);
        buttonArray[3][1] = new Button("8", op, output);
        buttonArray[3][2] = new Button("9", op, output);

        buttonArray[0][3].setBackground(Color.DARK_GRAY);
        buttonArray[0][1].setBackground(Color.DARK_GRAY);
        buttonArray[0][2].setBackground(Color.DARK_GRAY);

        buttonArray[1][3].setBackground(Color.DARK_GRAY);
        buttonArray[2][3].setBackground(Color.DARK_GRAY);
        buttonArray[3][3].setBackground(Color.DARK_GRAY);

        buttonArray[4][0].setBackground(Color.DARK_GRAY);
        buttonArray[4][2].setBackground(Color.DARK_GRAY);

        addButtons();
    }


    /**
     * Sets up the clear button which clears the calculation when pressed.
     */
    private JButton setUpClearButton() {
        JButton clear = new JButton("CLEAR");
        clear.setPreferredSize(new Dimension(75, 40));
        clear.setBackground(Color.red);
        clear.addActionListener(e -> op.hardClear());

        return clear;
    }

    private void addButtons() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++){
                add(buttonArray[i][j]);
            }
        }
    }
    static class Button extends JButton { //I guess you could make a number button and operation button, but that would take some restructuring...
        private final String symbol;
        private final Operations op;
        private final PrintStream output;

        public Button(String symbol, Operations op, PrintStream output) {
            super(symbol);
            this.output = output;
            setPreferredSize(new Dimension(60, 60));
            setBackground(Color.BLACK);
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
            setVisible(true);
            setForeground(Color.WHITE);

            this.symbol = symbol;
            this.op = op;
            this.addActionListener(e -> executeOperation());
        }

        private void executeOperation() {
            if (symbol.equals("=")) {
                op.evaluate();
                op.softClear();
                output.print(op.getResult());
            } else {
                op.parse(symbol);
                output.print(symbol);
            }
        }
    }
}











