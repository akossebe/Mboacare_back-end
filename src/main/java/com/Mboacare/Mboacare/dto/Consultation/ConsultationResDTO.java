package com.Mboacare.Mboacare.dto.Consultation;


import com.Mboacare.Mboacare.enums.StatutConsultation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ConsultationResDTO {
    private Long idConsultation;
    private LocalDate dateConsultation;
    private LocalTime heureConsultation;
    private String motif;
    private String diagnostic;
    private String observations;
    private StatutConsultation statut;
    private Long idRendezVous;
    private Long idPatient;
    private Long idMedecin;
}