package esg.esgdocbuilder.exception;

import esg.esgdocbuilder.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse(
            String message,
            int status,
            LocalDateTime timestamp
    ) {
    }

    private ResponseEntity<ErrorResponse> buildResponse(String message, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(
                message,
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }


    @ExceptionHandler({
            ProductNotFoundException.class,
            CategoryNotFoundException.class,
            InvoiceItemNotFoundException.class,
            RoleNotFoundExecption.class,
            UserDoesNotExistsException.class,
            InvoiceNotFoundException.class,
            RefreshTokenNotFoundException.class,
            AccountNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            UserAlreadyExistsException.class,
            PasswordDoNotMatchException.class,
            FileIsEmptyException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobal(Exception ex) {
        return buildResponse(
                "Internal server error: " + ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}