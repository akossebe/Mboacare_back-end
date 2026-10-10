package com.logonedigital.MBOAcare.repositoy;

import com.logonedigital.MBOAcare.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepo extends JpaRepository<Stock, String> {

    // Attention : plusieurs stocks peuvent maintenant porter le meme nom (dans des pharmacies differentes)
    Optional<Stock> findByNom(String nom);

    boolean existsByNomIgnoreCaseAndPharmaci_IdPharmaci(String nom, String idPharmaci);

    boolean existsByNomIgnoreCaseAndPharmaci_IdPharmaciAndIdStockNot(String nom, String idPharmaci, String idStock);
}



