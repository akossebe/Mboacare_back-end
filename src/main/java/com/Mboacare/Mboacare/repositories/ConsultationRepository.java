package com.Mboacare.Mboacare.repositories;

import com.Mboacare.Mboacare.entities.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation>findByIdPatient(Long idPatient);


    List<Consultation> findByIdMedecin(Long idMedecin);
    // Optional<...> = "peut-etre qu'il n'y a pas de resultat". C'est le cas
    // ici car un rendez-vous donne n'a pas TOUJOURS de consultation associee.
    Optional<Consultation> findByIdRendezVous(Long idRendezVous);
}
