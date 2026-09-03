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

/**
 * @RestControllerAdvice = cette classe surveille TOUS les controllers
 * de l'application. Des qu'une exception est levee quelque part (dans
 * un Service, un Controller...) et qu'elle n'est pas "attrapee" avant,
 * elle atterrit ici, dans la methode correspondant a son type.
 *
 * Avantage enorme : on n'a JAMAIS besoin d'ecrire de try/catch dans
 * les controllers ou les services. On se contente de faire
 * "throw new ResourceNotFoundException(...)" et cette classe s'occupe
 * de transformer ça en une belle reponse HTTP.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- Cas 1 : ressource introuvable (id inexistant) -> HTTP 404 ---
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

    // --- Cas 2 : regle metier violee (mauvaise transition de statut) -> HTTP 400 ---
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

    // --- Cas 3 : donnees invalides envoyees par le client (@Valid a echoue) -> HTTP 400 ---
    // Exemple : le client envoie un motifPrise vide alors qu'il est obligatoire.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> gererValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, Object> reponse = new LinkedHashMap<>();
        reponse.put("timestamp", LocalDateTime.now());
        reponse.put("statut", HttpStatus.BAD_REQUEST.value());
        reponse.put("erreur", "Donnees invalides");
        reponse.put("chemin", request.getRequestURI());

        // On construit un detail champ par champ : { "motifPrise": "ne doit pas etre vide" }
        Map<String, String> details = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            details.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        reponse.put("details", details);

        return new ResponseEntity<>(reponse, HttpStatus.BAD_REQUEST);
    }

    // --- Cas 4 (filet de securite) : toute autre erreur imprevue -> HTTP 500 ---
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
