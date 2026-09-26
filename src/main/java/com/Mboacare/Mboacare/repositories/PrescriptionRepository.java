package com.Mboacare.Mboacare.repositories;
import com.Mboacare.Mboacare.entities.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
public interface PrescriptionRepository extends JpaRepository<Prescription, Long>{
    List<Prescription> findByIdMedecin(Long idMedecin);
    List<Prescription> findByIdPharmacie(Long idPharmacie);
    Optional<Prescription> findByIdConsultation(Long idConsultation);
    Page<Prescription> findByIdMedecin(Long idMedecin, Pageable pageable);

    @Query("SELECT p FROM Prescription p WHERE p.idConsultation IN " +
           "(SELECT c.idConsultation FROM Consultation c WHERE c.idPatient = :idPatient)")
    Page<Prescription> findByIdPatient(@Param("idPatient") Long idPatient, Pageable pageable);
}
