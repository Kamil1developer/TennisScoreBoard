package exceptions;

public class TextValidationException extends RuntimeException {
    public TextValidationException(String message) {
        super(message);
    }
}
