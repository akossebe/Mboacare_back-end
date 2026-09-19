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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class RendezVousServiceImpl implements RendezVousService{


    private final RendezVousRepository rendezVousRepository;



    @Override
    public RendezVousResDTO creer(RendezVousReqDTO dto) {
        if (rendezVousRepository.existsByMedecinAndDateAndHeureAndNotCancelled(
                dto.getIdMedecin(), dto.getDateSouhaitee(), dto.getHeureSouhaitee())) {
            throw new BusinessRuleException("Ce créneau est déjà pris par un autre rendez-vous.");
        }

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
    public Page<RendezVousResDTO> getTous(Pageable pageable) {
        return rendezVousRepository.findAll(pageable)
                .map(this::versDTO);
    }

    @Override
    public void supprimer(Long id) {
        RendezVous rendezVous = trouverOuLeverErreur(id);
        rendezVousRepository.delete(rendezVous);
    }


    @Override
    public RendezVousResDTO prendreRendezVous(RendezVousReqDTO dto) {
        return creer(dto);
    }

    @Override
    public RendezVousResDTO confirmerRendezVous(Long id) {
        RendezVous rendezVous = trouverOuLeverErreur(id);


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


        if (rendezVous.getStatut() == StatutRendezVous.ANNULE
                || rendezVous.getStatut() == StatutRendezVous.HONORE) {
            throw new BusinessRuleException(
                    "Impossible de reporter un rendez-vous ayant le statut : " + rendezVous.getStatut()
            );
        }

        rendezVous.setDateSouhaitee(dto.getNouvelleDateSouhaitee());
        rendezVous.setHeureSouhaitee(dto.getNouvelleHeureSouhaitee());

        rendezVous.setStatut(StatutRendezVous.EN_ATTENTE);

        return versDTO(rendezVousRepository.save(rendezVous));
    }

    @Override
    public RendezVousResDTO annulerRendezVous(Long id) {
        RendezVous rendezVous = trouverOuLeverErreur(id);


        if (rendezVous.getStatut() == StatutRendezVous.HONORE) {
            throw new BusinessRuleException("Impossible d'annuler un rendez-vous deja honore");
        }

        rendezVous.setStatut(StatutRendezVous.ANNULE);
        return versDTO(rendezVousRepository.save(rendezVous));
    }




    private RendezVous trouverOuLeverErreur(Long id) {
        return rendezVousRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rendez-vous introuvable avec l'id : " + id));
    }

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