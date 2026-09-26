package com.Mboacare.Mboacare.services.consultation.consultation;
import com.Mboacare.Mboacare.dto.Consultation.CompteRenduDTO;
import com.Mboacare.Mboacare.dto.Consultation.ConsultationReqDTO;
import com.Mboacare.Mboacare.dto.Consultation.ConsultationResDTO;
import com.Mboacare.Mboacare.dto.Consultation.DiagnosticReqDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface ConsultationService {
    ConsultationResDTO getParId(Long id);
    Page<ConsultationResDTO> getTous(Pageable pageable, Long idPatient, Long idMedecin);
    void supprimer(Long id);
    ConsultationResDTO creerConsultation(ConsultationReqDTO dto);
    ConsultationResDTO enregistrerDiagnostic(Long id, DiagnosticReqDTO dto);
    ConsultationResDTO cloturerConsultation(Long id);
    CompteRenduDTO genererCompteRendu(Long id);
}
