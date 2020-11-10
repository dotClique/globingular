package globingular.core;

/**
 * Thrown to indicate that a data structure which requires uniquness
 * of some property has been passed duplicate values for that property.
 */
public class DuplicateIdentifierException extends IllegalArgumentException {
    /**
     * Construct a new DuplicateIdentifierException.
     */
    public DuplicateIdentifierException() {
        super();
    }

    /**
     * Construct a new DuplicateIdentifierException.
     *
     * @param s String to go in super's constructor
     */
    public DuplicateIdentifierException(final String s) {
        super(s);
    }

    /**
     * Construct a new DuplicateIdentifierException.
     *
     * @param message Message to include with the exception
     * @param cause   The cause of the exception
     */
    public DuplicateIdentifierException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct a new DuplicateIdentifierException.
     *
     * @param cause The cause of the exception
     */
    public DuplicateIdentifierException(final Throwable cause) {
        super(cause);
    }
}
