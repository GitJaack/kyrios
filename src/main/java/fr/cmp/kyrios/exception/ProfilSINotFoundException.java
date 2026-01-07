package fr.cmp.kyrios.exception;

public class ProfilSINotFoundException extends RuntimeException {
    public ProfilSINotFoundException(String message) {
        super(message);
    }

    public ProfilSINotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
