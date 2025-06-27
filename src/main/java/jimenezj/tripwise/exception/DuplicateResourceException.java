package jimenezj.tripwise.exception;

// This class represents an exception that is thrown when a duplicate resource is encountered.
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
