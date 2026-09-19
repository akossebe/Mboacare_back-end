package com.Mboacare.Mboacare.services.consultation.prescription;


import com.Mboacare.Mboacare.dto.Prescription.EnvoyerPharmacieDTO;
import com.Mboacare.Mboacare.dto.Prescription.PrescriptionReqDTO;
import com.Mboacare.Mboacare.dto.Prescription.PrescriptionResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PrescriptionService {

    Page<PrescriptionResDTO> getTous(Pageable pageable);
    void supprimer(Long id);



    PrescriptionResDTO rediger(PrescriptionReqDTO dto);
    PrescriptionResDTO valider(Long id);
    PrescriptionResDTO envoyerAPharmacie(Long id, EnvoyerPharmacieDTO dto);
    PrescriptionResDTO consulter(Long id);
    PrescriptionResDTO marquerDelivree(Long id);
}
