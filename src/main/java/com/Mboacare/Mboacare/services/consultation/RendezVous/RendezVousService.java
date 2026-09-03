package com.Mboacare.Mboacare.services.consultation.RendezVous;



import com.Mboacare.Mboacare.dto.RendezVous.RendezVousReqDTO;
import com.Mboacare.Mboacare.dto.RendezVous.RendezVousResDTO;
import com.Mboacare.Mboacare.dto.RendezVous.ReporterRendezVousDTO;

import java.util.List;

/**
 * L'interface decrit CE QUE le service sait faire, sans dire COMMENT.
 * C'est une bonne pratique : le Controller ne connait que cette interface,
 * jamais l'implementation concrete (RendezVousServiceImpl).
 */
public interface RendezVousService {

    // --- CRUD de base ---
    RendezVousResDTO creer(RendezVousReqDTO dto);          // Create
    RendezVousResDTO getParId(Long id);                        // Read (un seul)
    List<RendezVousResDTO> getTous();                          // Read (tous)
    void supprimer(Long id);                                        // Delete

    // --- Methodes metier du cahier des charges ---
    RendezVousResDTO prendreRendezVous(RendezVousReqDTO dto);
    RendezVousResDTO reporterRendezVous(Long id, ReporterRendezVousDTO dto);
    RendezVousResDTO annulerRendezVous(Long id);
    RendezVousResDTO confirmerRendezVous(Long id);
}