package com.logonedigital.MBOAcare.service.pharmaci;

import com.logonedigital.MBOAcare.Exception.ResourceExistException;
import com.logonedigital.MBOAcare.Exception.ResourceNotFoundException;
import com.logonedigital.MBOAcare.dto.*;
import com.logonedigital.MBOAcare.entity.Pharmaci;
import com.logonedigital.MBOAcare.repositoy.PharmaciRepo;
import com.logonedigital.MBOAcare.repositoy.StockRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service

public class PharmaciServiceImpl implements PharmaciService {

    private final PharmaciRepo pharmaciRepo;


    public PharmaciServiceImpl(PharmaciRepo pharmaciRepo) {
        this.pharmaciRepo = pharmaciRepo;

    }



    private PharmaciResdto toResdto(Pharmaci p) {
        return new PharmaciResdto(
                p.getIdPharmaci(),
                p.getNom(),
                p.getVille(),
                p.getEmail(),
                p.getQuartier()
        );
    }

    @Override
    public void addPharmaci(PharmaciReqdto pharmaciReqdto) {
        String nom = pharmaciReqdto.getNom().trim();
        String ville = pharmaciReqdto.getVille().trim();
        String email = pharmaciReqdto.getEmail().trim();

        if (this.pharmaciRepo.existsByNomIgnoreCaseAndVilleIgnoreCase(nom, ville)) {
            throw new ResourceExistException("Cette pharmacie existe deja dans cette ville");
        }
        if (this.pharmaciRepo.findByEmail(email).isPresent()) {
            throw new ResourceExistException("Cette adresse email est deja utilisee");
        }

        Pharmaci pharmaci = new Pharmaci();
        pharmaci.setNom(nom);
        pharmaci.setEmail(email);
        pharmaci.setVille(ville);
        pharmaci.setQuartier(pharmaciReqdto.getQuartier().trim());
        pharmaci.setDateCreation(LocalDate.now());

        this.pharmaciRepo.save(pharmaci);
    }

    @Override
    public List<PharmaciResdto> getAllPharmaci() {
        return this.pharmaciRepo.findAll().stream().map(this::toResdto).toList();
    }

    @Override
    public void updatePharmaci(String idPharmaci, PharmaciReqdto pharmaciReqdto) {
        Pharmaci oldPharmaci = this.pharmaciRepo.findById(idPharmaci)
                .orElseThrow(() -> new ResourceNotFoundException("cette pharmacie n existe pas"));

        String nom = pharmaciReqdto.getNom().trim();
        String ville = pharmaciReqdto.getVille().trim();
        String email = pharmaciReqdto.getEmail().trim();

        if (this.pharmaciRepo.existsByNomIgnoreCaseAndVilleIgnoreCaseAndIdPharmaciNot(nom, ville, idPharmaci)) {
            throw new ResourceExistException("Une autre pharmacie porte deja ce nom dans cette ville");
        }
        this.pharmaciRepo.findByEmail(email).ifPresent(autre -> {
            if (!autre.getIdPharmaci().equals(idPharmaci)) {
                throw new ResourceExistException("Cette adresse email est deja utilisee");
            }
        });

        oldPharmaci.setNom(nom);
        oldPharmaci.setVille(ville);
        oldPharmaci.setEmail(email);
        oldPharmaci.setQuartier(pharmaciReqdto.getQuartier().trim());

        this.pharmaciRepo.saveAndFlush(oldPharmaci);
    }

    @Override
    public Page<PharmaciResdto> getPaginated(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return pharmaciRepo.findAll(pageable).map(this::toResdto);
    }

    @Override
    public List<Pharmaci> findPharmaciByMedicamentNom(String nomMedicament) {
        return pharmaciRepo.findPharmaciByMedicamentNom(nomMedicament);
    }

    @Override
    public List<Object[]> countMedicamentParPharmaci() {
        return pharmaciRepo.countStockParPharmaci();
    }

    @Transactional
    public void deletePharmaci(String id) {
        Pharmaci pharmaci = pharmaciRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacie non trouvée"));
        pharmaciRepo.delete(pharmaci); // Hibernate supprime les stocks automatiquement
    }
    @Override
    public PharmaciResdto getPharmaciById(String idPharmaci) {

        Pharmaci pharmaci = this.pharmaciRepo.findById(idPharmaci)
                .orElseThrow(() -> new ResourceNotFoundException("cette pharmacie n existe pas"));

        PharmaciResdto pharmaciResdto = new PharmaciResdto();
        pharmaciResdto.setIdPharmaci(pharmaci.getIdPharmaci());
        pharmaciResdto.setNom(pharmaci.getNom());
        pharmaciResdto.setEmail(pharmaci.getEmail());
        pharmaciResdto.setVille(pharmaci.getVille());
        pharmaciResdto.setQuartier(pharmaci.getQuartier());

        return pharmaciResdto;
    }
}




