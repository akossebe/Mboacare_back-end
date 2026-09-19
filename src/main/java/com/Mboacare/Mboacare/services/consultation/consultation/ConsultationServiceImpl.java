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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final RendezVousRepository rendezVousRepository;


    @Override
    @Transactional
    public ConsultationResDTO getParId(Long id) {
        return versDTO(trouverOuLeverErreur(id));
    }

    @Override
    @Transactional
    public Page<ConsultationResDTO> getTous(Pageable pageable) {
        return consultationRepository.findAll(pageable).map(this::versDTO);
    }


    @Override
    @Transactional
    public void supprimer(Long id) {
        Consultation consultation = trouverOuLeverErreur(id);
        consultationRepository.delete(consultation);
    }


    @Override
    @Transactional
    public ConsultationResDTO creerConsultation(ConsultationReqDTO dto) {

        RendezVous rendezVous = rendezVousRepository.findById(dto.getIdRendezVous())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rendez-vous introuvable avec l'id : " + dto.getIdRendezVous()));


        if (rendezVous.getStatut() != StatutRendezVous.CONFIRME) {
            throw new BusinessRuleException(
                    "Le rendez-vous doit etre CONFIRME avant de creer une consultation (statut actuel : "
                            + rendezVous.getStatut() + ")");
        }


        if (consultationRepository.findByIdRendezVous(rendezVous.getIdRendezVous()).isPresent()) {
            throw new BusinessRuleException(
                    "Une consultation existe deja pour ce rendez-vous");
        }


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


        rendezVous.setStatut(StatutRendezVous.HONORE);
        rendezVousRepository.save(rendezVous);

        return versDTO(sauvegardee);
    }

    @Override
    @Transactional
    public ConsultationResDTO enregistrerDiagnostic(Long id, DiagnosticReqDTO dto) {
        Consultation consultation = trouverOuLeverErreur(id);


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
