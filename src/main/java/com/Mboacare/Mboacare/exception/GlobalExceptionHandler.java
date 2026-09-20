package com.Mboacare.Mboacare.exception;
import com.Mboacare.Mboacare.exception.BusinessRuleException;
import com.Mboacare.Mboacare.exception.ErrorResponse;
import com.Mboacare.Mboacare.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> gererResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorResponse erreur = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Ressource introuvable",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(erreur, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> gererBusinessRule(
            BusinessRuleException ex, HttpServletRequest request) {
        ErrorResponse erreur = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Regle metier non respectee",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(erreur, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> gererValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> reponse = new LinkedHashMap<>();
        reponse.put("timestamp", LocalDateTime.now());
        reponse.put("statut", HttpStatus.BAD_REQUEST.value());
        reponse.put("erreur", "Donnees invalides");
        reponse.put("chemin", request.getRequestURI());
        Map<String, String> details = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            details.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        reponse.put("details", details);
        return new ResponseEntity<>(reponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> gererErreurGenerique(
            Exception ex, HttpServletRequest request) {
        ErrorResponse erreur = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erreur interne du serveur",
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(erreur, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
