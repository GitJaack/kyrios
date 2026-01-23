package fr.cmp.kyrios.exception;

public class ProfilAppNotFoundException extends RuntimeException {
    public ProfilAppNotFoundException(String message) {
        super(message);
    }

    public ProfilAppNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
