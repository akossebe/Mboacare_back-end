package com.Mboacare.Mboacare.services.pharmacie;

import com.Mboacare.Mboacare.dto.PharmaciReqdto;
import com.Mboacare.Mboacare.dto.PharmaciResdto;
import com.Mboacare.Mboacare.entities.Pharmaci;
import com.Mboacare.Mboacare.exception.ResourceExistException;
import com.Mboacare.Mboacare.exception.ResourceNotFoundException;
import com.Mboacare.Mboacare.repositories.PharmaciRepo;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PharmaciServiceImpl implements PharmaciService {

    private final PharmaciRepo pharmaciRepo;

    public PharmaciServiceImpl(PharmaciRepo pharmaciRepo) {
        this.pharmaciRepo = pharmaciRepo;
    }

    // Construit la réponse : l'ordre des paramètres est (id, nom, ville, email, quartier)
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
    public void addPharmaci(PharmaciReqdto dto) {
        String nom = dto.getNom().trim();
        String ville = dto.getVille().trim();
        String quartier = dto.getQuartier().trim();
        String email = dto.getEmail().trim();

        if (pharmaciRepo.existsByNomIgnoreCaseAndVilleIgnoreCase(nom, ville)) {
            throw new ResourceExistException("Cette pharmacie existe déjà dans cette ville");
        }
        if (pharmaciRepo.existsByEmailIgnoreCase(email)) {
            throw new ResourceExistException("Cette adresse e-mail est déjà utilisée par une autre pharmacie");
        }

        Pharmaci pharmaci = new Pharmaci();
        pharmaci.setNom(nom);
        pharmaci.setEmail(email);
        pharmaci.setVille(ville);
        pharmaci.setQuartier(quartier);
        pharmaci.setDateCreation(LocalDate.now());

        this.pharmaciRepo.save(pharmaci);
    }

    @Override
    public PharmaciResdto getPharmaciById(String idPharmaci) {
        Pharmaci pharmaci = this.pharmaciRepo.findById(idPharmaci)
                .orElseThrow(() -> new ResourceNotFoundException("cette pharmacie n existe pas"));
        return toResdto(pharmaci);
    }

    @Override
    public List<PharmaciResdto> getAllPharmaci() {
        return this.pharmaciRepo.findAll().stream().map(this::toResdto).toList();
    }

    @Override
    public void updatePharmaci(String idPharmaci, PharmaciReqdto dto) {
        Pharmaci oldPharmaci = this.pharmaciRepo.findById(idPharmaci)
                .orElseThrow(() -> new ResourceNotFoundException("cette pharmacie n existe pas"));

        String nom = dto.getNom().trim();
        String ville = dto.getVille().trim();
        String quartier = dto.getQuartier().trim();
        String email = dto.getEmail().trim();

        if (pharmaciRepo.existsByNomIgnoreCaseAndVilleIgnoreCaseAndIdPharmaciNot(nom, ville, idPharmaci)) {
            throw new ResourceExistException("Cette pharmacie existe déjà dans cette ville");
        }
        if (pharmaciRepo.existsByEmailIgnoreCaseAndIdPharmaciNot(email, idPharmaci)) {
            throw new ResourceExistException("Cette adresse e-mail est déjà utilisée par une autre pharmacie");
        }

        oldPharmaci.setNom(nom);
        oldPharmaci.setVille(ville);
        oldPharmaci.setEmail(email);
        oldPharmaci.setQuartier(quartier);

        this.pharmaciRepo.saveAndFlush(oldPharmaci);
    }

    @Override
    @Transactional
    public void deletePharmaci(String id) {
        Pharmaci pharmaci = pharmaciRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pharmacie non trouvée"));
        pharmaciRepo.delete(pharmaci); // Hibernate supprime les stocks automatiquement
    }

    @Override
    public Page<PharmaciResdto> getPaginated(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Pharmaci> pharmaciPage = pharmaciRepo.findAll(pageable);

        return pharmaciPage.map(this::toResdto);
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