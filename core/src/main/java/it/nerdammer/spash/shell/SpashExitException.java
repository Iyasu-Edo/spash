package it.nerdammer.spash.shell;

/**
 * An exception that signal the willingness to exit the shell.
 *
 * @author Nicola Ferraro
 */
public class SpashExitException extends RuntimeException {

    public SpashExitException() {
    }

    public SpashExitException(String message) {
        super(message);
    }

    public SpashExitException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpashExitException(Throwable cause) {
        super(cause);
    }

    public SpashExitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
