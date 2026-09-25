package com.logonedigital.MBOAcare.repositoy;


import com.logonedigital.MBOAcare.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


    public interface StockRepo  extends JpaRepository<Stock, String> {
        Optional<Stock> findByNom(String nom);
    }




