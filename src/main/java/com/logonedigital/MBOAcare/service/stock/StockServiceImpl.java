package com.logonedigital.MBOAcare.service.stock;

import com.logonedigital.MBOAcare.Exception.ResourceExistException;
import com.logonedigital.MBOAcare.Exception.ResourceNotFoundException;
import com.logonedigital.MBOAcare.dto.StockReqdto;
import com.logonedigital.MBOAcare.dto.StockResdto;
import com.logonedigital.MBOAcare.entity.Pharmaci;
import com.logonedigital.MBOAcare.entity.Stock;
import com.logonedigital.MBOAcare.repositoy.PharmaciRepo;
import com.logonedigital.MBOAcare.repositoy.StockRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepo stockRepo;
    private final PharmaciRepo pharmaciRepo;

    public StockServiceImpl(StockRepo stockRepo, PharmaciRepo pharmaciRepo) {
        this.stockRepo = stockRepo;
        this.pharmaciRepo = pharmaciRepo;
    }

    private StockResdto toResdto(Stock stock) {
        Pharmaci p = stock.getPharmaci();
        return new StockResdto(
                stock.getIdStock(),
                stock.getQuantite(),
                stock.getNom(),
                p != null ? p.getIdPharmaci() : null,
                p != null ? p.getNom() : null
        );
    }

    private Pharmaci findPharmaci(String idPharmaci) {
        return this.pharmaciRepo.findById(idPharmaci)
                .orElseThrow(() -> new ResourceNotFoundException("Cette pharmacie n existe pas"));
    }

    @Override
    public void addStock(StockReqdto stockReqdto) {
        Pharmaci pharmaci = findPharmaci(stockReqdto.getIdPharmaci());
        String nom = stockReqdto.getNom().trim();

        if (this.stockRepo.existsByNomIgnoreCaseAndPharmaci_IdPharmaci(nom, pharmaci.getIdPharmaci())) {
            throw new ResourceExistException("Ce stock existe deja dans cette pharmacie");
        }

        Stock stock = new Stock();
        stock.setNom(nom);
        stock.setQuantite(stockReqdto.getQuantite());
        stock.setPharmaci(pharmaci);

        this.stockRepo.save(stock);
    }

    @Override
    public StockResdto getStockById(String idStock) {
        Stock stock = this.stockRepo.findById(idStock)
                .orElseThrow(() -> new ResourceNotFoundException("ce stock n existe pas"));
        return toResdto(stock);
    }

    @Override
    public List<StockResdto> getAllStock() {
        return this.stockRepo.findAll().stream().map(this::toResdto).toList();
    }

    @Override
    public void updateStock(String idStock, StockReqdto stockReqdto) {
        Stock oldStock = this.stockRepo.findById(idStock)
                .orElseThrow(() -> new ResourceNotFoundException("Ce stock n'existe pas"));

        Pharmaci pharmaci = findPharmaci(stockReqdto.getIdPharmaci());
        String nom = stockReqdto.getNom().trim();

        if (this.stockRepo.existsByNomIgnoreCaseAndPharmaci_IdPharmaciAndIdStockNot(nom, pharmaci.getIdPharmaci(), idStock)) {
            throw new ResourceExistException("Un autre stock porte deja ce nom dans cette pharmacie");
        }

        oldStock.setNom(nom);
        oldStock.setQuantite(stockReqdto.getQuantite());
        oldStock.setPharmaci(pharmaci);

        this.stockRepo.saveAndFlush(oldStock);
    }

    @Override
    public void deleteStock(String idStock) {
        Stock stock = this.stockRepo.findById(idStock)
                .orElseThrow(() -> new ResourceNotFoundException("Ce stock n existe pas !"));

        this.stockRepo.delete(stock);
    }

    @Override
    public Page<StockResdto> getPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return stockRepo.findAll(pageable).map(this::toResdto);
    }
}
