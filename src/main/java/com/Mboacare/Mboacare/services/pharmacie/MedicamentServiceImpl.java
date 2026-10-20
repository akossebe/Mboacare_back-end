package com.Mboacare.Mboacare.services.pharmacie;

import com.Mboacare.Mboacare.dto.MedicamentReqdto;
import com.Mboacare.Mboacare.dto.MedicamentResdto;
import com.Mboacare.Mboacare.entities.Medicament;
import com.Mboacare.Mboacare.entities.Pharmaci;
import com.Mboacare.Mboacare.entities.Stock;
import com.Mboacare.Mboacare.exception.ResourceExistException;
import com.Mboacare.Mboacare.exception.ResourceNotFoundException;
import com.Mboacare.Mboacare.repositories.MedicamentRepo;
import com.Mboacare.Mboacare.repositories.StockRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicamentServiceImpl implements MedicamentService {

    private final MedicamentRepo medicamentRepo;
    private final StockRepo stockRepo;

    public MedicamentServiceImpl(MedicamentRepo medicamentRepo, StockRepo stockRepo) {
        this.medicamentRepo = medicamentRepo;
        this.stockRepo = stockRepo;
    }

    private MedicamentResdto toResdto(Medicament m) {
        Stock s = m.getStock();
        Pharmaci p = (s != null) ? s.getPharmaci() : null;
        return new MedicamentResdto(
                m.getIdMedicament(),
                m.getNom(),
                m.getForme(),
                m.getPrix(),
                s != null ? s.getIdStock() : null,
                s != null ? s.getNom() : null,
                s != null ? s.getQuantite() : null,
                p != null ? p.getNom() : null,
                p != null ? p.getVille() : null
        );
    }

    private Stock findStock(String idStock) {
        return this.stockRepo.findById(idStock)
                .orElseThrow(() -> new ResourceNotFoundException("Ce stock n'existe pas"));
    }

    @Override
    public void addMedicament(MedicamentReqdto dto) {
        String nom = dto.getNom().trim();
        Stock stock = findStock(dto.getIdStock());

        if (medicamentRepo.existsByNomIgnoreCaseAndStock_IdStock(nom, stock.getIdStock())) {
            throw new ResourceExistException("Ce médicament existe déjà dans ce stock");
        }

        Medicament medicament = new Medicament();
        medicament.setNom(nom);
        medicament.setForme(dto.getForme().trim());
        medicament.setPrix(dto.getPrix());
        medicament.setStock(stock);

        this.medicamentRepo.save(medicament);
    }

    @Override
    public MedicamentResdto getMedicamentById(String idMedicament) {
        Medicament medicament = this.medicamentRepo.findById(idMedicament)
                .orElseThrow(() -> new ResourceNotFoundException("ce medicament n existe pas"));
        return toResdto(medicament);
    }

    @Override
    public List<MedicamentResdto> getAllMedicament() {
        return this.medicamentRepo.findAll().stream().map(this::toResdto).toList();
    }

    @Override
    public void updateMedicament(String idMedicament, MedicamentReqdto dto) {
        Medicament oldMedicament = this.medicamentRepo.findById(idMedicament)
                .orElseThrow(() -> new ResourceNotFoundException("Ce medicament n'existe pas"));

        String nom = dto.getNom().trim();
        Stock stock = findStock(dto.getIdStock());

        if (medicamentRepo.existsByNomIgnoreCaseAndStock_IdStockAndIdMedicamentNot(nom, stock.getIdStock(), idMedicament)) {
            throw new ResourceExistException("Ce médicament existe déjà dans ce stock");
        }

        oldMedicament.setNom(nom);
        oldMedicament.setForme(dto.getForme().trim());
        oldMedicament.setPrix(dto.getPrix());
        oldMedicament.setStock(stock);

        this.medicamentRepo.saveAndFlush(oldMedicament);
    }

    @Override
    public void deleteMedicament(String idMedicament) {
        Medicament medicament = this.medicamentRepo.findById(idMedicament)
                .orElseThrow(() -> new ResourceNotFoundException("Ce medicament n existe pas !"));
        this.medicamentRepo.delete(medicament);
    }

    @Override
    public Page<MedicamentResdto> getPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return medicamentRepo.findAll(pageable).map(this::toResdto);
    }

    @Override
    public List<MedicamentResdto> findMedicamentByForme(String forme) {
        return medicamentRepo.findMedicamentByForme(forme).stream().map(this::toResdto).toList();
    }
}