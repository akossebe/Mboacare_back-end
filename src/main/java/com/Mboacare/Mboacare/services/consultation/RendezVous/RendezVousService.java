package com.Mboacare.Mboacare.services.consultation.RendezVous;
import com.Mboacare.Mboacare.dto.RendezVous.RendezVousReqDTO;
import com.Mboacare.Mboacare.dto.RendezVous.RendezVousResDTO;
import com.Mboacare.Mboacare.dto.RendezVous.ReporterRendezVousDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
public interface RendezVousService {
    RendezVousResDTO creer(RendezVousReqDTO dto);
    RendezVousResDTO getParId(Long id);
    Page<RendezVousResDTO> getTous(Pageable pageable, Long idPatient, Long idMedecin);
    List<LocalTime> getCreneauxOccupes(Long idMedecin, LocalDate date);
    void supprimer(Long id);
    RendezVousResDTO prendreRendezVous(RendezVousReqDTO dto);
    RendezVousResDTO reporterRendezVous(Long id, ReporterRendezVousDTO dto);
    RendezVousResDTO annulerRendezVous(Long id);
    RendezVousResDTO confirmerRendezVous(Long id);
}
