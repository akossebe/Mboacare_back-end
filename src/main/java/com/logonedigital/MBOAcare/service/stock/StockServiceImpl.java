package com.logonedigital.MBOAcare.service.stock;

import com.logonedigital.MBOAcare.Exception.ResourceExistException;
import com.logonedigital.MBOAcare.Exception.ResourceNotFoundException;
import com.logonedigital.MBOAcare.dto.StockReqdto;
import com.logonedigital.MBOAcare.dto.StockResdto;
import com.logonedigital.MBOAcare.entity.Medicament;
import com.logonedigital.MBOAcare.entity.Pharmaci;
import com.logonedigital.MBOAcare.entity.Stock;
import com.logonedigital.MBOAcare.repositoy.MedicamentRepo;
import com.logonedigital.MBOAcare.repositoy.PharmaciRepo;
import com.logonedigital.MBOAcare.repositoy.StockRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class StockServiceImpl implements StockService {
    private final StockRepo stockRepo;



    public StockServiceImpl(StockRepo stockRepo) {
        this.stockRepo = stockRepo;

    }

    @Override



    public void addStock(StockReqdto stockReqdto) {

        // Vérifier si le stock existe déjà
        Optional<Stock> stockFound = this.stockRepo.findByNom(stockReqdto.getNom());

        if (stockFound.isPresent()) {
            throw new ResourceExistException("Ce stock existe déjà");
        }

        // Création du stock
        Stock stock = new Stock();
        stock.setNom(stockReqdto.getNom());
        stock.setQuantite(stockReqdto.getQuantite());

        this.stockRepo.save(stock);

    }

    @Override
    public StockResdto getStockById(String idStock) {

        Stock stock = this.stockRepo.findById(idStock).orElseThrow(() -> new ResourceNotFoundException("ce stock n existe pas"));
        return new StockResdto(stock.getIdStock(), stock.getQuantite(), stock.getNom());

    }


    @Override
    public List<StockResdto> getAllStock() {
        return this.stockRepo.findAll().stream().map(stock -> {

            return new StockResdto(stock.getIdStock(), stock.getQuantite(), stock.getNom());
        }).toList();


    }

    @Override
    public void updateStock(String idStock, StockReqdto stockReqdto) {
        Stock oldStock = this.stockRepo.findById(idStock)
                .orElseThrow(() -> new ResourceNotFoundException("Ce stock n'existe pas"));

        oldStock.setQuantite(stockReqdto.getQuantite());
        oldStock.setNom(stockReqdto.getNom());
        this.stockRepo.saveAndFlush(oldStock);

    }


    @Override
    public void deleteStock(String idStock) {
        Stock stock = this.stockRepo.findById(idStock).orElseThrow(() -> new ResourceNotFoundException("Ce stock n existe pas !"));

        this.stockRepo.delete(stock);
    }
    @Override
    public Page<StockResdto> getPaginated(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());

        Page<Stock> stockPage = stockRepo.findAll(pageable);

        return stockPage.map(stock -> new StockResdto(
                stock.getIdStock(),
                stock.getQuantite(),
                stock.getNom()

        ));
    }
}
