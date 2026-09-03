package com.Mboacare.Mboacare.exception;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Chaque fois qu'une erreur se produit, on renvoie TOUJOURS un objet
 * de cette forme au client, pour que le frontend (Angular) puisse
 * afficher un message clair, quel que soit le type d'erreur.
 *
 * Exemple de reponse JSON generee :
 * {
 *   "timestamp": "2026-09-02T10:15:30",
 *   "statut": 404,
 *   "erreur": "Ressource introuvable",
 *   "message": "Rendez-vous introuvable avec l'id : 999",
 *   "chemin": "/api/rendez-vous/999"
 * }
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ErrorResponse {
    private LocalDateTime timestamp;
    private int statut;
    private String erreur;
    private String message;
    private String chemin;
}
