package com.Mboacare.Mboacare.services.consultation.consultation;


import com.Mboacare.Mboacare.dto.Consultation.CompteRenduDTO;
import com.Mboacare.Mboacare.dto.Consultation.ConsultationReqDTO;
import com.Mboacare.Mboacare.dto.Consultation.ConsultationResDTO;
import com.Mboacare.Mboacare.dto.Consultation.DiagnosticReqDTO;
import com.Mboacare.Mboacare.entities.Consultation;
import com.Mboacare.Mboacare.entities.RendezVous;
import com.Mboacare.Mboacare.enums.StatutConsultation;
import com.Mboacare.Mboacare.enums.StatutRendezVous;
import com.Mboacare.Mboacare.exception.BusinessRuleException;
import com.Mboacare.Mboacare.exception.ResourceNotFoundException;
import com.Mboacare.Mboacare.repositories.ConsultationRepository;
import com.Mboacare.Mboacare.repositories.RendezVousRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    // On a aussi besoin du repository RendezVous car une consultation
    // nait TOUJOURS d'un rendez-vous confirme (regle metier).
    private final RendezVousRepository rendezVousRepository;

    // ======================= CRUD DE BASE =======================

    @Override
    @Transactional
    public ConsultationResDTO getParId(Long id) {
        return versDTO(trouverOuLeverErreur(id));
    }

    @Override
    @Transactional
    public List<ConsultationResDTO> getTous() {
        return consultationRepository.findAll().stream().map(this::versDTO).toList();
    }

    @Override
    @Transactional
    public void supprimer(Long id) {
        Consultation consultation = trouverOuLeverErreur(id);
        consultationRepository.delete(consultation);
    }

    // ======================= METHODES METIER =======================

    @Override
    @Transactional
    public ConsultationResDTO creerConsultation(ConsultationReqDTO dto) {
        // 1. On recupere le rendez-vous d'origine
        RendezVous rendezVous = rendezVousRepository.findById(dto.getIdRendezVous())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rendez-vous introuvable avec l'id : " + dto.getIdRendezVous()));

        // 2. REGLE METIER : on ne peut creer une consultation que si le
        // rendez-vous a ete CONFIRME au prealable.
        if (rendezVous.getStatut() != StatutRendezVous.CONFIRME) {
            throw new BusinessRuleException(
                    "Le rendez-vous doit etre CONFIRME avant de creer une consultation (statut actuel : "
                            + rendezVous.getStatut() + ")");
        }

        // 3. REGLE METIER : un rendez-vous ne peut donner naissance qu'a
        // une seule consultation.
        if (consultationRepository.findByIdRendezVous(rendezVous.getIdRendezVous()).isPresent()) {
            throw new BusinessRuleException(
                    "Une consultation existe deja pour ce rendez-vous");
        }

        // 4. Creation de la consultation, avec les infos reprises du rendez-vous
        Consultation consultation = Consultation.builder()
                .dateConsultation(LocalDate.now())
                .heureConsultation(LocalTime.now())
                .motif(dto.getMotif() != null ? dto.getMotif() : rendezVous.getMotifPrise())
                .statut(StatutConsultation.EN_COURS)
                .idRendezVous(rendezVous.getIdRendezVous())
                .idPatient(rendezVous.getIdPatient())
                .idMedecin(rendezVous.getIdMedecin())
                .build();

        Consultation sauvegardee = consultationRepository.save(consultation);

        // 5. Le rendez-vous est desormais "honore" (le patient s'est presente)
        rendezVous.setStatut(StatutRendezVous.HONORE);
        rendezVousRepository.save(rendezVous);

        return versDTO(sauvegardee);
    }

    @Override
    @Transactional
    public ConsultationResDTO enregistrerDiagnostic(Long id, DiagnosticReqDTO dto) {
        Consultation consultation = trouverOuLeverErreur(id);

        // REGLE METIER : on ne modifie le diagnostic que sur une consultation
        // encore EN_COURS (une consultation cloturee est figee).
        if (consultation.getStatut() != StatutConsultation.EN_COURS) {
            throw new BusinessRuleException(
                    "Impossible de modifier le diagnostic d'une consultation " + consultation.getStatut());
        }

        consultation.setDiagnostic(dto.getDiagnostic());
        consultation.setObservations(dto.getObservations());

        return versDTO(consultationRepository.save(consultation));
    }

    @Override
    @Transactional
    public ConsultationResDTO cloturerConsultation(Long id) {
        Consultation consultation = trouverOuLeverErreur(id);

        if (consultation.getStatut() == StatutConsultation.CLOTUREE) {
            throw new BusinessRuleException("Cette consultation est deja cloturee");
        }

        // REGLE METIER : impossible de cloturer sans diagnostic renseigne
        if (consultation.getDiagnostic() == null || consultation.getDiagnostic().isBlank()) {
            throw new BusinessRuleException(
                    "Impossible de cloturer une consultation sans diagnostic renseigne");
        }

        consultation.setStatut(StatutConsultation.CLOTUREE);
        return versDTO(consultationRepository.save(consultation));
    }

    @Override
    @Transactional
    public CompteRenduDTO genererCompteRendu(Long id) {
        Consultation consultation = trouverOuLeverErreur(id);

        // REGLE METIER : un compte-rendu n'a de sens que si la consultation
        // est terminee.
        if (consultation.getStatut() != StatutConsultation.CLOTUREE) {
            throw new BusinessRuleException(
                    "Le compte-rendu ne peut etre genere que pour une consultation cloturee");
        }

        String texte = String.format(
                "Compte-rendu de consultation du %s%nMotif : %s%nDiagnostic : %s%nObservations : %s",
                consultation.getDateConsultation(),
                consultation.getMotif(),
                consultation.getDiagnostic(),
                consultation.getObservations() != null ? consultation.getObservations() : "Aucune"
        );

        return CompteRenduDTO.builder()
                .idConsultation(consultation.getIdConsultation())
                .dateConsultation(consultation.getDateConsultation())
                .motif(consultation.getMotif())
                .diagnostic(consultation.getDiagnostic())
                .observations(consultation.getObservations())
                .contenuTextuel(texte)
                .build();
    }

    // ======================= METHODES PRIVEES UTILITAIRES =======================

    private Consultation trouverOuLeverErreur(Long id) {
        return consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation introuvable avec l'id : " + id));
    }

    private ConsultationResDTO versDTO(Consultation c) {
        return ConsultationResDTO.builder()
                .idConsultation(c.getIdConsultation())
                .dateConsultation(c.getDateConsultation())
                .heureConsultation(c.getHeureConsultation())
                .motif(c.getMotif())
                .diagnostic(c.getDiagnostic())
                .observations(c.getObservations())
                .statut(c.getStatut())
                .idRendezVous(c.getIdRendezVous())
                .idPatient(c.getIdPatient())
                .idMedecin(c.getIdMedecin())
                .build();
    }
}
