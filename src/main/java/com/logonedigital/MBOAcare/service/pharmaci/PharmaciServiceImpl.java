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



    @Override
    public void addPharmaci(PharmaciReqdto pharmaciReqdto) {

        Optional<Pharmaci> pharmaciFound =
                this.pharmaciRepo.findByEmail(pharmaciReqdto.getEmail());

        if (pharmaciFound.isPresent()) {
            throw new ResourceExistException("Cette pharmacie existe deja");
        }

        Pharmaci pharmaci= new Pharmaci();
        pharmaci.setNom(pharmaciReqdto.getNom());
        pharmaci.setEmail(pharmaciReqdto.getEmail());
        pharmaci.setVille(pharmaciReqdto.getVille());
        pharmaci.setQuartier(pharmaciReqdto.getQuartier());
        pharmaci.setDateCreation(LocalDate.now());


        this.pharmaciRepo.save(pharmaci);
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

    @Override
    public List<PharmaciResdto> getAllPharmaci() {
        return this.pharmaciRepo.findAll().stream().map(pharmaci -> {

            return new PharmaciResdto(pharmaci.getIdPharmaci(), pharmaci.getNom(), pharmaci.getEmail(),pharmaci.getVille(), pharmaci.getQuartier());
        }).toList();
    }

    @Override
    public void updatePharmaci(String idPharmaci, PharmaciReqdto pharmaciReqdto) {

        Pharmaci oldPharmaci = this.pharmaciRepo.findById(idPharmaci)
                .orElseThrow(() -> new ResourceNotFoundException("cette pharmacie n existe pas"));


        oldPharmaci.setNom(pharmaciReqdto.getNom());
        oldPharmaci.setVille(pharmaciReqdto.getVille());
        oldPharmaci.setEmail(pharmaciReqdto.getEmail());
        oldPharmaci.setQuartier(pharmaciReqdto.getQuartier());

        this.pharmaciRepo.saveAndFlush(oldPharmaci);


    }

    @Transactional
    public void deletePharmaci(String id) {
        Pharmaci pharmaci = pharmaciRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacie non trouvée"));
        pharmaciRepo.delete(pharmaci); // Hibernate supprime les stocks automatiquement
    }

    @Override
    public Page<PharmaciResdto> getPaginated(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Pharmaci> pharmaciPage = pharmaciRepo.findAll(pageable);

        return pharmaciPage.map(pharmaci -> new PharmaciResdto(
                pharmaci.getIdPharmaci(),
                pharmaci.getNom(),
                pharmaci.getVille(),
                pharmaci.getQuartier(),
                pharmaci.getEmail()
        ));
    }

    @Override
    public List<Pharmaci> findPharmaciByMedicamentNom(String nomMedicament) {
        return pharmaciRepo.findPharmaciByMedicamentNom(nomMedicament);
    }

    @Override
    public List<Object[]> countMedicamentParPharmaci() {
        return pharmaciRepo.countStockParPharmaci();
    }


}



