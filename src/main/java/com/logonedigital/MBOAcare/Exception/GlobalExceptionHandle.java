package com.logonedigital.MBOAcare.Exception;
import com.logonedigital.MBOAcare.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(Exception ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorMessage(
                        ex.getMessage(),
                        LocalDateTime.now(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        HttpStatus.NOT_FOUND.value()
                )
        );
    }

    @ExceptionHandler(ResourceExistException.class)
    public ResponseEntity<ErrorMessage> handleResourceExistException(Exception ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorMessage(
                        ex.getMessage(),
                        LocalDateTime.now(), 
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        HttpStatus.CONFLICT.value()
                )
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
