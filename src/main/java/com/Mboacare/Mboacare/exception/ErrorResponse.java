package com.Mboacare.Mboacare.exception;
import lombok.*;
import java.time.LocalDateTime;
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
