package com.Mboacare.Mboacare.repositories;

import com.Mboacare.Mboacare.entities.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation>findByIdPatient(Long idPatient);


    List<Consultation> findByIdMedecin(Long idMedecin);
    long countByIdMedecin(Long idMedecin);

    Optional<Consultation> findByIdRendezVous(Long idRendezVous);
}
