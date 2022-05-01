package Calculator;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class TextOutputStream extends OutputStream {
    private final JTextArea textArea;

    public TextOutputStream(JTextArea textArea) {
        this.textArea = textArea;
    }

    @Override
    public void write(int b) throws IOException {
        // redirects data to the text area
        textArea.append(String.valueOf((char)b));
        // scrolls the text area to the end of data
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
