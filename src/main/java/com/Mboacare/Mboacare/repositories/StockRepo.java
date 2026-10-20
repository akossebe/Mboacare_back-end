package com.Mboacare.Mboacare.repositories;

import com.Mboacare.Mboacare.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepo extends JpaRepository<Stock, String> {

    Optional<Stock> findByNom(String nom);

    boolean existsByNomIgnoreCaseAndPharmaci_IdPharmaci(String nom, String idPharmaci);

    boolean existsByNomIgnoreCaseAndPharmaci_IdPharmaciAndIdStockNot(String nom, String idPharmaci, String idStock);
}