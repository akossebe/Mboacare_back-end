package com.Mboacare.Mboacare.services;

import com.Mboacare.Mboacare.dto.MedecinStatsDTO;
import com.Mboacare.Mboacare.repositories.ConsultationRepository;
import com.Mboacare.Mboacare.repositories.RendezVousRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatistiquesService {

    private final ConsultationRepository consultationRepository;
    private final RendezVousRepository rendezVousRepository;

    public MedecinStatsDTO getStatsMedecin(Long idMedecin) {
        long nbConsultations = consultationRepository.countByIdMedecin(idMedecin);
        long nbRendezVousTotal = rendezVousRepository.countByIdMedecin(idMedecin);
        long nbAnnules = rendezVousRepository.countAnnulesByIdMedecin(idMedecin);

        double tauxAnnulation = 0.0;
        if (nbRendezVousTotal > 0) {
            tauxAnnulation = (double) nbAnnules / nbRendezVousTotal * 100;
        }

        return MedecinStatsDTO.builder()
                .idMedecin(idMedecin)
                .nombreConsultations(nbConsultations)
                .nombreRendezVousTotal(nbRendezVousTotal)
                .nombreRendezVousAnnules(nbAnnules)
                .tauxAnnulation(tauxAnnulation)
                .build();
    }
}
