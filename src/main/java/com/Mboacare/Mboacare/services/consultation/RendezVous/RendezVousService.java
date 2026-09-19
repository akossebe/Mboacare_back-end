package com.Mboacare.Mboacare.services.consultation.RendezVous;



import com.Mboacare.Mboacare.dto.RendezVous.RendezVousReqDTO;
import com.Mboacare.Mboacare.dto.RendezVous.RendezVousResDTO;
import com.Mboacare.Mboacare.dto.RendezVous.ReporterRendezVousDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RendezVousService {


    RendezVousResDTO creer(RendezVousReqDTO dto);
    RendezVousResDTO getParId(Long id);
    Page<RendezVousResDTO> getTous(Pageable pageable);
    void supprimer(Long id);


    RendezVousResDTO prendreRendezVous(RendezVousReqDTO dto);
    RendezVousResDTO reporterRendezVous(Long id, ReporterRendezVousDTO dto);
    RendezVousResDTO annulerRendezVous(Long id);
    RendezVousResDTO confirmerRendezVous(Long id);
}