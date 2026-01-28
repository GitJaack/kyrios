package fr.cmp.kyrios.exception;

public class RessourceAppNotFoundException extends RuntimeException {
    public RessourceAppNotFoundException(String message) {
        super(message);
    }

    public RessourceAppNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
