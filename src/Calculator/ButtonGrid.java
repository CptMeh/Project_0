package Calculator;

import javax.swing.*;
import java.awt.*;

import java.io.PrintStream;

public class ButtonGrid extends JPanel {
    private final JButton[][] buttonArray = new JButton[5][3];
    private final Operations op;
    private final PrintStream output;

    public ButtonGrid(Operations op, PrintStream output) {
        this.output = output;
        this.op = op;
        setLayout(new GridLayout(5, 3));
        setSize(400, 400);
        setUpButtons();
    }

    private void setUpButtons() {
        int number = 1;

        operationButtons();
        numberButtons(number);
    }

    private void operationButtons() {
        buttonArray[0][0] = new Button("/", op, output);
        buttonArray[1][0] = new Button("*", op, output);
        buttonArray[2][0] = new Button("+", op, output);

        buttonArray[2][2] = new Button(".", op, output);
        buttonArray[3][2] = new Button("0", op, output);
        buttonArray[4][2] = new Button("=", op, output);
    }

    private void numberButtons(int number) {
        for (int j = 0; j < 3; j++){
            for (int i = 0; i < 5; i++) {
                if (buttonArray[i][j] == null) {
                    buttonArray[i][j] = new Button("" + number, op, output);
                    number++;
                }
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
            setBackground(Color.GRAY);
            setBorder(BorderFactory.createLineBorder(Color.black));
            setVisible(true);

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











