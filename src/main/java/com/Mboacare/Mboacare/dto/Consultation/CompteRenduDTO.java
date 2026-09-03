package com.Mboacare.Mboacare.dto.Consultation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

    /**
     * Represente le "compte-rendu" genere a la fin d'une consultation
     * (methode genererCompteRendu() du cahier des charges). C'est un
     * simple resume textuel pour l'instant ; on pourra plus tard
     * transformer ça en vrai PDF telechargeable.
     */
    @Getter
    @Builder
    @AllArgsConstructor
    public class CompteRenduDTO {
        private Long idConsultation;
        private LocalDate dateConsultation;
        private String motif;
        private String diagnostic;
        private String observations;
        private String contenuTextuel; // texte pret a afficher/imprimer
    }


