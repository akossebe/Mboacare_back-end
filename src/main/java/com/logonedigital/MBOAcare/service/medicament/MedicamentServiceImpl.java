package com.logonedigital.MBOAcare.service.medicament;

import com.logonedigital.MBOAcare.Exception.ResourceExistException;
import com.logonedigital.MBOAcare.Exception.ResourceNotFoundException;
import com.logonedigital.MBOAcare.dto.MedicamentReqdto;
import com.logonedigital.MBOAcare.dto.MedicamentResdto;
import com.logonedigital.MBOAcare.entity.Medicament;
import com.logonedigital.MBOAcare.entity.Pharmaci;
import com.logonedigital.MBOAcare.entity.Stock;
import com.logonedigital.MBOAcare.repositoy.MedicamentRepo;
import com.logonedigital.MBOAcare.repositoy.StockRepo;
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
                s != null ? s.getQuantite() : 0,
                p != null ? p.getNom() : null,
                p != null ? p.getVille() : null
        );
    }

    private Stock findStock(String idStock) {
        return this.stockRepo.findById(idStock)
                .orElseThrow(() -> new ResourceNotFoundException("Ce stock n existe pas"));
    }

    @Override
    public void addMedicament(MedicamentReqdto medicamentReqdto) {
        Stock stock = findStock(medicamentReqdto.getIdStock());
        String nom = medicamentReqdto.getNom().trim();

        if (this.medicamentRepo.existsByNomIgnoreCaseAndStock_IdStock(nom, stock.getIdStock())) {
            throw new ResourceExistException("Ce medicament existe deja dans ce stock");
        }

        Medicament medicament = new Medicament();
        medicament.setNom(nom);
        medicament.setForme(medicamentReqdto.getForme().trim());
        medicament.setPrix(medicamentReqdto.getPrix());
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
    public void updateMedicament(String idMedicament, MedicamentReqdto medicamentReqdto) {
        Medicament oldMedicament = this.medicamentRepo.findById(idMedicament)
                .orElseThrow(() -> new ResourceNotFoundException("Ce medicament n'existe pas"));

        Stock stock = findStock(medicamentReqdto.getIdStock());
        String nom = medicamentReqdto.getNom().trim();

        if (this.medicamentRepo.existsByNomIgnoreCaseAndStock_IdStockAndIdMedicamentNot(nom, stock.getIdStock(), idMedicament)) {
            throw new ResourceExistException("Un autre medicament porte deja ce nom dans ce stock");
        }

        oldMedicament.setNom(nom);
        oldMedicament.setForme(medicamentReqdto.getForme().trim());
        oldMedicament.setPrix(medicamentReqdto.getPrix());
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

