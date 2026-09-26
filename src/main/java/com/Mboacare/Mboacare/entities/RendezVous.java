package com.Mboacare.Mboacare.entities;
import com.Mboacare.Mboacare.enums.StatutRendezVous;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Entity
@Table(name = "rendez_vous")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRendezVous;
    @Column(nullable = false)
    private LocalDate dateSouhaitee;
    @Column(nullable = false)
    private LocalTime heureSouhaitee;
    @Column(length = 500)
    private String motifPrise;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutRendezVous statut;
    @Column(nullable = false)
    private Long idPatient;
    @Column(nullable = false)
    private Long idMedecin;
    @Column(updatable = false)
    private LocalDateTime dateCreation;
    @PrePersist
    public void avantCreation() {
        this.dateCreation = LocalDateTime.now();
        if (this.statut == null) {
            this.statut = StatutRendezVous.EN_ATTENTE;
        }
    }
}
