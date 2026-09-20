package com.Mboacare.Mboacare.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Builder
public class MedecinStatsDTO {
    private Long idMedecin;
    private long nombreConsultations;
    private long nombreRendezVousTotal;
    private long nombreRendezVousAnnules;
    private double tauxAnnulation;
}
