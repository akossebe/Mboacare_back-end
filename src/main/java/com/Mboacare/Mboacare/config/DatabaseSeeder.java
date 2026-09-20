package com.Mboacare.Mboacare.config;
import com.Mboacare.Mboacare.entities.*;
import com.Mboacare.Mboacare.enums.*;
import com.Mboacare.Mboacare.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {
    private final RendezVousRepository rendezVousRepository;
    private final ConsultationRepository consultationRepository;
    private final PrescriptionRepository prescriptionRepository;
    @Override
    public void run(String... args) throws Exception {
        if (rendezVousRepository.count() == 0) {
            System.out.println("--- Seeding Database ---");
            RendezVous r1 = RendezVous.builder()
                    .dateSouhaitee(LocalDate.now().plusDays(2))
                    .heureSouhaitee(LocalTime.of(10, 0))
                    .motifPrise("Contrôle annuel")
                    .statut(StatutRendezVous.CONFIRME)
                    .idPatient(1L)
                    .idMedecin(1L)
                    .build();
            RendezVous r2 = RendezVous.builder()
                    .dateSouhaitee(LocalDate.now().plusDays(5))
                    .heureSouhaitee(LocalTime.of(14, 30))
                    .motifPrise("Renouvellement ordonnance")
                    .statut(StatutRendezVous.EN_ATTENTE)
                    .idPatient(1L)
                    .idMedecin(1L)
                    .build();
            RendezVous r3 = RendezVous.builder()
                    .dateSouhaitee(LocalDate.now().minusDays(30))
                    .heureSouhaitee(LocalTime.of(9, 15))
                    .motifPrise("Douleurs thoraciques")
                    .statut(StatutRendezVous.EFFECTUE)
                    .idPatient(1L)
                    .idMedecin(1L)
                    .build();
            rendezVousRepository.saveAll(List.of(r1, r2, r3));
            Consultation c1 = Consultation.builder()
                    .dateConsultation(r3.getDateSouhaitee())
                    .heureConsultation(r3.getHeureSouhaitee())
                    .motif("Douleurs thoraciques avec irradiation")
                    .diagnostic("Angine de poitrine (CIM-10: I20)")
                    .observations("Patient anxieux. Tension 140/90. ECG normal.")
                    .statut(StatutConsultation.CLOTUREE)
                    .idRendezVous(r3.getIdRendezVous())
                    .idPatient(r3.getIdPatient())
                    .idMedecin(r3.getIdMedecin())
                    .build();
            consultationRepository.save(c1);
            LigneMedicament lm1 = new LigneMedicament("Bisoprolol 2.5mg", "1 comprimé le matin", "1 mois", 30);
            LigneMedicament lm2 = new LigneMedicament("Kardegic 75mg", "1 sachet le midi", "1 mois", 30);
            Prescription p1 = Prescription.builder()
                    .dateEmission(c1.getDateConsultation())
                    .statut(StatutPrescription.EMISE)
                    .idConsultation(c1.getIdConsultation())
                    .idMedecin(c1.getIdMedecin())
                    .lignesMedicaments(List.of(lm1, lm2))
                    .build();
            prescriptionRepository.save(p1);
            System.out.println("--- Seeding Completed ---");
        }
    }
}
