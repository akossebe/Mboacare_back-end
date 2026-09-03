package com.Mboacare.Mboacare.services.consultation.RendezVous;


import com.Mboacare.Mboacare.dto.RendezVous.RendezVousReqDTO;
import com.Mboacare.Mboacare.dto.RendezVous.RendezVousResDTO;

import com.Mboacare.Mboacare.dto.RendezVous.ReporterRendezVousDTO;
import com.Mboacare.Mboacare.entities.RendezVous;
import com.Mboacare.Mboacare.enums.StatutRendezVous;
import com.Mboacare.Mboacare.exception.BusinessRuleException;
import com.Mboacare.Mboacare.exception.ResourceNotFoundException;
import com.Mboacare.Mboacare.repositories.RendezVousRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Service dit a Spring : "cree une instance de cette classe et garde-la
 * disponible pour l'injecter partout ou on en a besoin" (ici, dans le Controller).
 *
 * @RequiredArgsConstructor (Lombok) genere automatiquement un constructeur
 * qui prend tous les champs "final" en parametre -> c'est ce qu'on appelle
 * "l'injection de dependances par constructeur", la methode recommandee
 * par Spring (plutot que @Autowired sur le champ directement).
 */
@Service
@RequiredArgsConstructor
public class RendezVousServiceImpl implements RendezVousService{


    private final RendezVousRepository rendezVousRepository;

    // ======================= CRUD DE BASE =======================

    @Override
    public RendezVousResDTO creer(RendezVousReqDTO dto) {
        RendezVous rendezVous = RendezVous.builder()
                .dateSouhaitee(dto.getDateSouhaitee())
                .heureSouhaitee(dto.getHeureSouhaitee())
                .motifPrise(dto.getMotifPrise())
                .idPatient(dto.getIdPatient())
                .idMedecin(dto.getIdMedecin())
                .statut(StatutRendezVous.EN_ATTENTE)
                .build();

        RendezVous sauvegarde = rendezVousRepository.save(rendezVous);
        return versDTO(sauvegarde);
    }

    @Override
    public RendezVousResDTO getParId(Long id) {
        RendezVous rendezVous = trouverOuLeverErreur(id);
        return versDTO(rendezVous);
    }

    @Override
    public List<RendezVousResDTO> getTous() {
        return rendezVousRepository.findAll()
                .stream()
                .map(this::versDTO)
                .toList();
    }

    @Override
    public void supprimer(Long id) {
        // On verifie d'abord que le rendez-vous existe, sinon message clair
        RendezVous rendezVous = trouverOuLeverErreur(id);
        rendezVousRepository.delete(rendezVous);
    }

    // ======================= METHODES METIER =======================

    /**
     * prendreRendezVous() : dans notre implementation, "prendre" un
     * rendez-vous revient exactement a le "creer". On garde les deux
     * methodes separees dans l'interface pour rester fidele au vocabulaire
     * du cahier des charges, mais elles font la meme chose ici.
     */
    @Override
    public RendezVousResDTO prendreRendezVous(RendezVousReqDTO dto) {
        return creer(dto);
    }

    @Override
    public RendezVousResDTO confirmerRendezVous(Long id) {
        RendezVous rendezVous = trouverOuLeverErreur(id);

        // REGLE METIER : on ne peut confirmer que si le rendez-vous
        // est actuellement EN_ATTENTE.
        if (rendezVous.getStatut() != StatutRendezVous.EN_ATTENTE) {
            throw new BusinessRuleException(
                    "Impossible de confirmer un rendez-vous ayant le statut : " + rendezVous.getStatut()
            );
        }

        rendezVous.setStatut(StatutRendezVous.CONFIRME);
        return versDTO(rendezVousRepository.save(rendezVous));
    }

    @Override
    public RendezVousResDTO reporterRendezVous(Long id, ReporterRendezVousDTO dto) {
        RendezVous rendezVous = trouverOuLeverErreur(id);

        // REGLE METIER : on ne peut pas reporter un rendez-vous deja
        // annule ou deja honore (termine).
        if (rendezVous.getStatut() == StatutRendezVous.ANNULE
                || rendezVous.getStatut() == StatutRendezVous.HONORE) {
            throw new BusinessRuleException(
                    "Impossible de reporter un rendez-vous ayant le statut : " + rendezVous.getStatut()
            );
        }

        rendezVous.setDateSouhaitee(dto.getNouvelleDateSouhaitee());
        rendezVous.setHeureSouhaitee(dto.getNouvelleHeureSouhaitee());
        // Un rendez-vous reporte repasse automatiquement en attente de confirmation
        rendezVous.setStatut(StatutRendezVous.EN_ATTENTE);

        return versDTO(rendezVousRepository.save(rendezVous));
    }

    @Override
    public RendezVousResDTO annulerRendezVous(Long id) {
        RendezVous rendezVous = trouverOuLeverErreur(id);

        // REGLE METIER : un rendez-vous deja honore ne peut plus etre annule
        // (la consultation a deja eu lieu).
        if (rendezVous.getStatut() == StatutRendezVous.HONORE) {
            throw new BusinessRuleException("Impossible d'annuler un rendez-vous deja honore");
        }

        rendezVous.setStatut(StatutRendezVous.ANNULE);
        return versDTO(rendezVousRepository.save(rendezVous));
    }

    // ======================= METHODES PRIVEES UTILITAIRES =======================

    /**
     * Cherche un rendez-vous par id. S'il n'existe pas, on leve directement
     * ResourceNotFoundException -> le GlobalExceptionHandler transformera
     * ça en reponse HTTP 404 automatiquement.
     */
    private RendezVous trouverOuLeverErreur(Long id) {
        return rendezVousRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rendez-vous introuvable avec l'id : " + id));
    }

    /** Convertit une Entity (RendezVous) en DTO a renvoyer au client. */
    private RendezVousResDTO versDTO(RendezVous r) {
        return RendezVousResDTO.builder()
                .idRendezVous(r.getIdRendezVous())
                .dateSouhaitee(r.getDateSouhaitee())
                .heureSouhaitee(r.getHeureSouhaitee())
                .motifPrise(r.getMotifPrise())
                .statut(r.getStatut())
                .idPatient(r.getIdPatient())
                .idMedecin(r.getIdMedecin())
                .dateCreation(r.getDateCreation())
                .build();
    }
}
