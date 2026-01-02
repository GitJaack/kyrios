package fr.cmp.kyrios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(EmploiNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleEmploiNotFound(EmploiNotFoundException ex, WebRequest request) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(ex.getMessage())
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(ex.getMessage())
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex, WebRequest request) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(ex.getMessage())
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                        WebRequest request) {
                String message = ex.getBindingResult().getFieldErrors().stream()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .reduce((a, b) -> a + ", " + b)
                                .orElse("Erreur de validation");

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(message)
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message("Une erreur interne s'est produite")
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleInvalidEnum(HttpMessageNotReadableException ex, WebRequest request) {
                String message = "JSON invalide";

                Throwable cause = ex.getCause();
                if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException invalidEx) {
                        Object invalidValue = invalidEx.getValue();
                        Class<?> targetType = invalidEx.getTargetType();

                        if (targetType.isEnum()) {
                                // Nom du champ JSON
                                String fieldName = "inconnu";
                                if (!invalidEx.getPath().isEmpty()) {
                                        fieldName = invalidEx.getPath().get(0).getFieldName();
                                }

                                // Toutes les valeurs possibles
                                Object[] possibleValues = targetType.getEnumConstants();
                                message = "Valeur invalide pour le champ \""
                                                + fieldName + "\": \"" + invalidValue + "\". "
                                                + "Valeurs possibles : " + java.util.Arrays.toString(possibleValues);
                        }
                }

                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(message)
                                .build();

                return ResponseEntity.badRequest().body(errorResponse);
        }

}