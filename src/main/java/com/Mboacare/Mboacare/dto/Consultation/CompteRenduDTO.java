package com.Mboacare.Mboacare.dto.Consultation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
    @Getter
    @Builder
    @AllArgsConstructor
    public class CompteRenduDTO {
        private Long idConsultation;
        private LocalDate dateConsultation;
        private String motif;
        private String diagnostic;
        private String observations;
        private String contenuTextuel; 
    }
