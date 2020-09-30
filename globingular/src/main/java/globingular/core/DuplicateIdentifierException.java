package globingular.core;

public class DuplicateIdentifierException extends IllegalArgumentException {
    public DuplicateIdentifierException() {
        super();
    }

    public DuplicateIdentifierException(String s) {
        super(s);
    }

    public DuplicateIdentifierException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateIdentifierException(Throwable cause) {
        super(cause);
    }
}
