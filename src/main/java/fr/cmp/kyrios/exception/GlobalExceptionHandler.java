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

        @ExceptionHandler(ProfilSINotFoundException.class)
        public ResponseEntity<ErrorResponse> handleProfilSINotFound(ProfilSINotFoundException ex, WebRequest request) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(ex.getMessage())
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(RessourceSINotFoundException.class)
        public ResponseEntity<ErrorResponse> handleRessourceSINotFound(RessourceSINotFoundException ex,
                        WebRequest request) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(ex.getMessage())
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(CategorieNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleCategorieNotFound(CategorieNotFoundException ex,
                        WebRequest request) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(ex.getMessage())
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(AppNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleAppNotFound(AppNotFoundException ex, WebRequest request) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(ex.getMessage())
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(ProfilAppNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleProfilAppNotFound(ProfilAppNotFoundException ex,
                        WebRequest request) {
                ErrorResponse errorResponse = ErrorResponse.builder()
                                .message(ex.getMessage())
                                .build();

                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(RessourceAppNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleRessourceAppNotFound(RessourceAppNotFoundException ex,
                        WebRequest request) {
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

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                        WebRequest request) {
                String message = "Format JSON invalide";

                Throwable cause = ex.getCause();

                if (cause != null) {
                        // Cas générique pour toutes les autres causes
                        String causeMessage = cause.getMessage();
                        if (causeMessage != null && causeMessage.contains("not one of the values accepted for Enum")) {
                                // Extraire le nom du champ et les valeurs de l'enum depuis le message
                                int valuesStart = causeMessage.indexOf('[');
                                int valuesEnd = causeMessage.indexOf(']');
                                if (valuesStart != -1 && valuesEnd != -1) {
                                        String values = causeMessage.substring(valuesStart + 1, valuesEnd);
                                        message = String.format("status: Valeurs acceptées: %s",
                                                        values);
                                }
                        }
                }

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
}