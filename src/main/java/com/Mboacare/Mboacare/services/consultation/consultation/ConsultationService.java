package com.Mboacare.Mboacare.services.consultation.consultation;




import com.Mboacare.Mboacare.dto.Consultation.CompteRenduDTO;
import com.Mboacare.Mboacare.dto.Consultation.ConsultationReqDTO;
import com.Mboacare.Mboacare.dto.Consultation.ConsultationResDTO;
import com.Mboacare.Mboacare.dto.Consultation.DiagnosticReqDTO;

import java.util.List;

public interface ConsultationService {

    // --- CRUD de base ---
    ConsultationResDTO getParId(Long id);
    List<ConsultationResDTO> getTous();
    void supprimer(Long id);

    // --- Methodes metier du cahier des charges ---
    ConsultationResDTO creerConsultation(ConsultationReqDTO dto);
    ConsultationResDTO enregistrerDiagnostic(Long id, DiagnosticReqDTO dto);
    ConsultationResDTO cloturerConsultation(Long id);
    CompteRenduDTO genererCompteRendu(Long id);
}
