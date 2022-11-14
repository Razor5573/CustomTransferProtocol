package exceptions;

public class WrongPortException extends RuntimeException{
    public WrongPortException(String message) {
        super(message);
    }
}