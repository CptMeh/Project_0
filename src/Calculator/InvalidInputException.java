package Calculator;

public class InvalidInputException extends Exception{

    public InvalidInputException() {
        super("Invalid usage of operation signs.");
    }
}
