package com.Mboacare.Mboacare.services.consultation.RendezVous;
import com.Mboacare.Mboacare.dto.RendezVous.RendezVousReqDTO;
import com.Mboacare.Mboacare.dto.RendezVous.RendezVousResDTO;
import com.Mboacare.Mboacare.dto.RendezVous.ReporterRendezVousDTO;
import com.Mboacare.Mboacare.entities.RendezVous;
import com.Mboacare.Mboacare.enums.StatutRendezVous;
import com.Mboacare.Mboacare.exception.BusinessRuleException;
import com.Mboacare.Mboacare.exception.ResourceNotFoundException;
import com.Mboacare.Mboacare.repositories.RendezVousRepository;
import com.Mboacare.Mboacare.repositories.UtilisateurRepository;
import com.Mboacare.Mboacare.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class RendezVousServiceImpl implements RendezVousService{
    private final RendezVousRepository rendezVousRepository;
    private final NotificationService notificationService;
    private final UtilisateurRepository utilisateurRepository;

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

        // Fetch patient for name
        String nomPatient = utilisateurRepository.findById(sauvegarde.getIdPatient())
                .map(u -> u.getPrenom() + " " + u.getNom())
                .orElse("Un patient");

        // Fetch doctor for name
        String nomMedecin = utilisateurRepository.findById(sauvegarde.getIdMedecin())
                .map(u -> "Dr. " + u.getPrenom() + " " + u.getNom())
                .orElse("Le médecin");

        // Notifier le médecin
        notificationService.notifyMedecin(sauvegarde.getIdMedecin(), 
            "Nouvelle demande de RDV", 
            "Le patient " + nomPatient + " a demandé un rendez-vous le " + sauvegarde.getDateSouhaitee() + " à " + sauvegarde.getHeureSouhaitee() + ".\nMotif : " + sauvegarde.getMotifPrise());
        
        // Notifier le patient (lui-même)
        notificationService.notifyPatient(sauvegarde.getIdPatient(), 
            "Demande de RDV envoyée", 
            "Vous êtes en attente de confirmation de votre rendez-vous avec le " + nomMedecin + " pour le " + sauvegarde.getDateSouhaitee() + " à " + sauvegarde.getHeureSouhaitee() + ".");

        return versDTO(sauvegarde);
    }
    @Override
    public RendezVousResDTO getParId(Long id) {
        RendezVous rendezVous = trouverOuLeverErreur(id);
        return versDTO(rendezVous);
    }
    @Override
    public Page<RendezVousResDTO> getTous(Pageable pageable, Long idPatient, Long idMedecin) {
        if (idPatient != null && idMedecin != null) {
            return rendezVousRepository.findByIdPatientAndIdMedecin(idPatient, idMedecin, pageable)
                    .map(this::versDTO);
        }
        if (idPatient != null) {
            return rendezVousRepository.findByIdPatient(idPatient, pageable).map(this::versDTO);
        }
        if (idMedecin != null) {
            return rendezVousRepository.findByIdMedecin(idMedecin, pageable).map(this::versDTO);
        }
        return rendezVousRepository.findAll(pageable)
                .map(this::versDTO);
    }

    @Override
    public List<LocalTime> getCreneauxOccupes(Long idMedecin, LocalDate date) {
        return rendezVousRepository.findHeuresOccupees(idMedecin, date);
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
        RendezVous sauvegarde = rendezVousRepository.save(rendezVous);
        
        String nomMedecin = utilisateurRepository.findById(sauvegarde.getIdMedecin())
                .map(u -> "Dr. " + u.getPrenom() + " " + u.getNom() + " (" + u.getSpecialite() + ")")
                .orElse("Le médecin");

        notificationService.notifyPatient(sauvegarde.getIdPatient(), 
            "Rendez-vous confirmé", 
            "Votre rendez-vous a été confirmé par le " + nomMedecin + ". \n" +
            "Détails : Le " + sauvegarde.getDateSouhaitee() + " à " + sauvegarde.getHeureSouhaitee() + ".\n" +
            "Motif : " + sauvegarde.getMotifPrise());
        return versDTO(sauvegarde);
    }
    @Override
    public RendezVousResDTO reporterRendezVous(Long id, ReporterRendezVousDTO dto) {
        RendezVous rendezVous = trouverOuLeverErreur(id);
        if (rendezVous.getStatut() == StatutRendezVous.ANNULE
                || rendezVous.getStatut() == StatutRendezVous.EFFECTUE) {
            throw new BusinessRuleException(
                    "Impossible de reporter un rendez-vous ayant le statut : " + rendezVous.getStatut()
            );
        }
        rendezVous.setDateSouhaitee(dto.getNouvelleDateSouhaitee());
        rendezVous.setHeureSouhaitee(dto.getNouvelleHeureSouhaitee());
        rendezVous.setStatut(StatutRendezVous.EN_ATTENTE);
        
        RendezVous sauvegarde = rendezVousRepository.save(rendezVous);
        
        String nomMedecin = utilisateurRepository.findById(sauvegarde.getIdMedecin())
                .map(u -> "Dr. " + u.getPrenom() + " " + u.getNom())
                .orElse("Le médecin");

        notificationService.notifyPatient(sauvegarde.getIdPatient(), 
            "Rendez-vous reporté", 
            nomMedecin + " a reporté votre rendez-vous au " + sauvegarde.getDateSouhaitee() + " à " + sauvegarde.getHeureSouhaitee());

        return versDTO(sauvegarde);
    }
    @Override
    public RendezVousResDTO annulerRendezVous(Long id) {
        RendezVous rendezVous = trouverOuLeverErreur(id);
        if (rendezVous.getStatut() == StatutRendezVous.EFFECTUE) {
            throw new BusinessRuleException("Impossible d'annuler un rendez-vous deja honore");
        }
        rendezVous.setStatut(StatutRendezVous.ANNULE);
        
        RendezVous sauvegarde = rendezVousRepository.save(rendezVous);

        String nomMedecin = utilisateurRepository.findById(sauvegarde.getIdMedecin())
                .map(u -> "Dr. " + u.getPrenom() + " " + u.getNom())
                .orElse("Le médecin");

        notificationService.notifyPatient(sauvegarde.getIdPatient(), 
            "Rendez-vous annulé", 
            nomMedecin + " a malheureusement annulé votre rendez-vous du " + sauvegarde.getDateSouhaitee());

        return versDTO(sauvegarde);
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
