package com.Mboacare.Mboacare.services.consultation.prescription;


import com.Mboacare.Mboacare.dto.Prescription.EnvoyerPharmacieDTO;
import com.Mboacare.Mboacare.dto.Prescription.PrescriptionReqDTO;
import com.Mboacare.Mboacare.dto.Prescription.PrescriptionResDTO;

import java.util.List;

public interface PrescriptionService {

    // --- CRUD de base ---
    List<PrescriptionResDTO> getTous();
    void supprimer(Long id);

    // --- Methodes metier du cahier des charges ---
    PrescriptionResDTO rediger(PrescriptionReqDTO dto);
    PrescriptionResDTO valider(Long id);
    PrescriptionResDTO envoyerAPharmacie(Long id, EnvoyerPharmacieDTO dto);
    PrescriptionResDTO consulter(Long id);
    PrescriptionResDTO marquerDelivree(Long id);
}
