package ra.project_module04.exception;

public class NoOrdersFoundException extends RuntimeException{
    public NoOrdersFoundException(String message) {
        super(message);
    }
}
