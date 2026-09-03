package com.Mboacare.Mboacare.entities;

import com.Mboacare.Mboacare.enums.StatutConsultation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "consultation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConsultation;

    @Column(nullable = false)
    private LocalDate dateConsultation;

    @Column(nullable = false)
    private LocalTime heureConsultation;

    @Column(length = 500)
    private  String motif;

    @Column(length = 2000)
    private String diagnostic;

    @Column(length = 2000)
    private  String observations;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutConsultation statut;

    private Long idRendezVous;

    @Column(nullable = false)
    private Long idPatient;

    @Column(nullable = false)
    private long idMedecin;

    @PrePersist
    public void avantCreation (){
        if (this.statut == null){
            this.statut = StatutConsultation.EN_COURS;
        }
    }
}
