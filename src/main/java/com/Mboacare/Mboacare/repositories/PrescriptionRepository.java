package com.Mboacare.Mboacare.repositories;

import com.Mboacare.Mboacare.entities.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long>{

    List<Prescription> findByIdMedecin(Long idMedecin);

    List<Prescription> findByIdPharmacie(Long idPharmacie);

    Optional<Prescription> findByIdConsultation(Long idConsultation);
}
