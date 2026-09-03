package com.Mboacare.Mboacare.repositories;

import com.Mboacare.Mboacare.entities.RendezVous;
import com.Mboacare.Mboacare.enums.StatutRendezVous;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {

    // Genere automatiquement : SELECT * FROM rendez_vous WHERE id_medecin = ?
    List<RendezVous> findByIdMedecin(Long idMedecin);

    // Genere automatiquement : SELECT * FROM rendez_vous WHERE id_patient = ?
    List<RendezVous> findByIdPatient(Long idPatient);

    // Genere automatiquement : SELECT * FROM rendez_vous WHERE statut = ?
    List<RendezVous> findByStatut(StatutRendezVous statut);
}
