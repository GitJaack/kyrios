package fr.cmp.kyrios.exception;

public class CategorieNotFoundException extends RuntimeException {
    public CategorieNotFoundException(String message) {
        super(message);
    }

    public CategorieNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}