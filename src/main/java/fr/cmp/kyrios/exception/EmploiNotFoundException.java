package fr.cmp.kyrios.exception;

public class EmploiNotFoundException extends RuntimeException {
    public EmploiNotFoundException(String message) {
        super(message);
    }

    public EmploiNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
