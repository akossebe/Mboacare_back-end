package com.Mboacare.Mboacare.repositories;
import com.Mboacare.Mboacare.entities.RendezVous;
import com.Mboacare.Mboacare.enums.StatutRendezVous;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
    @Query("SELECT COUNT(r) > 0 FROM RendezVous r WHERE r.idMedecin = :idMedecin " +
           "AND r.dateSouhaitee = :date AND r.heureSouhaitee = :heure " +
           "AND r.statut NOT IN (com.Mboacare.Mboacare.enums.StatutRendezVous.ANNULE)")
    boolean existsByMedecinAndDateAndHeureAndNotCancelled(@Param("idMedecin") Long idMedecin,
                                                          @Param("date") LocalDate date,
                                                          @Param("heure") LocalTime heure);
    long countByIdMedecin(Long idMedecin);
    @Query("SELECT COUNT(r) FROM RendezVous r WHERE r.idMedecin = :idMedecin AND r.statut = com.Mboacare.Mboacare.enums.StatutRendezVous.ANNULE")
    long countAnnulesByIdMedecin(@Param("idMedecin") Long idMedecin);

    Page<RendezVous> findByIdPatient(Long idPatient, Pageable pageable);
    Page<RendezVous> findByIdMedecin(Long idMedecin, Pageable pageable);
    Page<RendezVous> findByIdPatientAndIdMedecin(Long idPatient, Long idMedecin, Pageable pageable);

    @Query("SELECT r.heureSouhaitee FROM RendezVous r WHERE r.idMedecin = :idMedecin " +
           "AND r.dateSouhaitee = :date " +
           "AND r.statut NOT IN (com.Mboacare.Mboacare.enums.StatutRendezVous.ANNULE)")
    List<LocalTime> findHeuresOccupees(@Param("idMedecin") Long idMedecin,
                                       @Param("date") LocalDate date);
}
