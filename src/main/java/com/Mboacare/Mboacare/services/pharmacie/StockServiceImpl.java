package com.Mboacare.Mboacare.services.pharmacie;

import com.Mboacare.Mboacare.dto.StockReqdto;
import com.Mboacare.Mboacare.dto.StockResdto;
import com.Mboacare.Mboacare.entities.Pharmaci;
import com.Mboacare.Mboacare.entities.Stock;
import com.Mboacare.Mboacare.exception.ResourceExistException;
import com.Mboacare.Mboacare.exception.ResourceNotFoundException;
import com.Mboacare.Mboacare.repositories.PharmaciRepo;
import com.Mboacare.Mboacare.repositories.StockRepo;
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

    private StockResdto toResdto(Stock s) {
        Pharmaci p = s.getPharmaci();
        return new StockResdto(
                s.getIdStock(),
                s.getQuantite(),
                s.getNom(),
                p != null ? p.getIdPharmaci() : null,
                p != null ? p.getNom() : null
        );
    }

    private Pharmaci findPharmaci(String idPharmaci) {
        return this.pharmaciRepo.findById(idPharmaci)
                .orElseThrow(() -> new ResourceNotFoundException("Cette pharmacie n'existe pas"));
    }

    @Override
    public void addStock(StockReqdto dto) {
        String nom = dto.getNom().trim();
        Pharmaci pharmaci = findPharmaci(dto.getIdPharmaci());

        if (stockRepo.existsByNomIgnoreCaseAndPharmaci_IdPharmaci(nom, pharmaci.getIdPharmaci())) {
            throw new ResourceExistException("Ce stock existe déjà dans cette pharmacie");
        }

        Stock stock = new Stock();
        stock.setNom(nom);
        stock.setQuantite(dto.getQuantite());
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
    public void updateStock(String idStock, StockReqdto dto) {
        Stock oldStock = this.stockRepo.findById(idStock)
                .orElseThrow(() -> new ResourceNotFoundException("Ce stock n'existe pas"));

        String nom = dto.getNom().trim();
        Pharmaci pharmaci = findPharmaci(dto.getIdPharmaci());

        if (stockRepo.existsByNomIgnoreCaseAndPharmaci_IdPharmaciAndIdStockNot(nom, pharmaci.getIdPharmaci(), idStock)) {
            throw new ResourceExistException("Ce stock existe déjà dans cette pharmacie");
        }

        oldStock.setNom(nom);
        oldStock.setQuantite(dto.getQuantite());
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