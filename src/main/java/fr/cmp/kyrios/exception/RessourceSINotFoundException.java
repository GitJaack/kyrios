package fr.cmp.kyrios.exception;

public class RessourceSINotFoundException extends RuntimeException {
    public RessourceSINotFoundException(String message) {
        super(message);
    }

    public RessourceSINotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
