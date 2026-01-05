package fr.cmp.kyrios.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}