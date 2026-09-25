package com.logonedigital.MBOAcare.repositoy;

import com.logonedigital.MBOAcare.entity.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicamentRepo extends JpaRepository<Medicament, String> {
    Optional<Medicament> findByNom(String nom);

    // Trouver les médicaments par forme
    @Query("SELECT m FROM Medicament m WHERE m.forme = :forme")
    List<Medicament> findMedicamentByForme(@Param("forme") String forme);


    Optional<Medicament> findByNomAndStock_IdStock(String nom, String idStock);
}

