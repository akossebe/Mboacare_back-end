package com.Mboacare.Mboacare.services.consultation.prescription;



import com.Mboacare.Mboacare.dto.Prescription.EnvoyerPharmacieDTO;
import com.Mboacare.Mboacare.dto.Prescription.LigneMedicamentDTO;
import com.Mboacare.Mboacare.dto.Prescription.PrescriptionReqDTO;
import com.Mboacare.Mboacare.dto.Prescription.PrescriptionResDTO;
import com.Mboacare.Mboacare.entities.Consultation;
import com.Mboacare.Mboacare.entities.LigneMedicament;
import com.Mboacare.Mboacare.entities.Prescription;
import com.Mboacare.Mboacare.enums.StatutConsultation;
import com.Mboacare.Mboacare.enums.StatutPrescription;
import com.Mboacare.Mboacare.exception.BusinessRuleException;
import com.Mboacare.Mboacare.exception.ResourceNotFoundException;
import com.Mboacare.Mboacare.repositories.ConsultationRepository;
import com.Mboacare.Mboacare.repositories.PrescriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

        private final PrescriptionRepository prescriptionRepository;
        private final ConsultationRepository consultationRepository;

        @Override
        public Page<PrescriptionResDTO> getTous(Pageable pageable) {
            return prescriptionRepository.findAll(pageable).map(this::versDTO);
        }

        @Override
        public void supprimer(Long id) {
            Prescription prescription = trouverOuLeverErreur(id);
            prescriptionRepository.delete(prescription);
        }




        @Override
        public PrescriptionResDTO rediger(PrescriptionReqDTO dto) {

            Consultation consultation = consultationRepository.findById(dto.getIdConsultation())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Consultation introuvable avec l'id : " + dto.getIdConsultation()));


            if (consultation.getStatut() != StatutConsultation.CLOTUREE) {
                throw new BusinessRuleException(
                        "La consultation doit etre CLOTUREE avant de rediger une prescription");
            }


            if (prescriptionRepository.findByIdConsultation(consultation.getIdConsultation()).isPresent()) {
                throw new BusinessRuleException("Une prescription existe deja pour cette consultation");
            }


            List<LigneMedicament> lignes = dto.getLignesMedicaments().stream()
                    .map(l -> new LigneMedicament(l.getNomMedicament(), l.getPosologie(), l.getDuree(), l.getQuantite()))
                    .toList();

            Prescription prescription = Prescription.builder()
                    .dateEmission(LocalDate.now())
                    .lignesMedicaments(lignes)
                    .statut(StatutPrescription.EMISE)
                    .idConsultation(consultation.getIdConsultation())
                    .idMedecin(consultation.getIdMedecin())
                    .build();

            return versDTO(prescriptionRepository.save(prescription));
        }

        @Override
        public PrescriptionResDTO valider(Long id) {
            Prescription prescription = trouverOuLeverErreur(id);

            if (prescription.getStatut() != StatutPrescription.EMISE) {
                throw new BusinessRuleException(
                        "Seule une prescription EMISE peut etre validee (statut actuel : "
                                + prescription.getStatut() + ")");
            }
            if (prescription.getLignesMedicaments().isEmpty()) {
                throw new BusinessRuleException("La prescription doit contenir au moins un medicament");
            }


            return versDTO(prescription);
        }

        @Override
        public PrescriptionResDTO envoyerAPharmacie(Long id, EnvoyerPharmacieDTO dto) {
            Prescription prescription = trouverOuLeverErreur(id);


            if (prescription.getStatut() != StatutPrescription.EMISE) {
                throw new BusinessRuleException(
                        "Seule une prescription EMISE peut etre envoyee a une pharmacie (statut actuel : "
                                + prescription.getStatut() + ")");
            }

            prescription.setIdPharmacie(dto.getIdPharmacie());
            prescription.setStatut(StatutPrescription.TRANSMISE);

            return versDTO(prescriptionRepository.save(prescription));
        }

        @Override
        public PrescriptionResDTO consulter(Long id) {
            return versDTO(trouverOuLeverErreur(id));
        }

        @Override
        public PrescriptionResDTO marquerDelivree(Long id) {
            Prescription prescription = trouverOuLeverErreur(id);


            if (prescription.getStatut() != StatutPrescription.TRANSMISE) {
                throw new BusinessRuleException(
                        "Seule une prescription TRANSMISE peut etre marquee comme delivree (statut actuel : "
                                + prescription.getStatut() + ")");
            }

            prescription.setStatut(StatutPrescription.DELIVREE);
            return versDTO(prescriptionRepository.save(prescription));
        }



        private Prescription trouverOuLeverErreur(Long id) {
            return prescriptionRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Prescription introuvable avec l'id : " + id));
        }

        private PrescriptionResDTO versDTO(Prescription p) {
            List<LigneMedicamentDTO> lignesDTO = p.getLignesMedicaments().stream()
                    .map(l -> new LigneMedicamentDTO(l.getNomMedicament(), l.getPosologie(), l.getDuree(), l.getQuantite()))
                    .toList();

            return PrescriptionResDTO.builder()
                    .idPrescription(p.getIdPrescription())
                    .dateEmission(p.getDateEmission())
                    .lignesMedicaments(lignesDTO)
                    .statut(p.getStatut())
                    .idConsultation(p.getIdConsultation())
                    .idMedecin(p.getIdMedecin())
                    .idPharmacie(p.getIdPharmacie())
                    .build();
        }


}
