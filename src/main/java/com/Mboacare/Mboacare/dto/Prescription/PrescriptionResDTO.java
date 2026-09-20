package com.Mboacare.Mboacare.dto.Prescription;
import com.Mboacare.Mboacare.enums.StatutPrescription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
public class PrescriptionResDTO {
    private Long idPrescription;
    private LocalDate dateEmission;
    private List<LigneMedicamentDTO> lignesMedicaments;
    private StatutPrescription statut;
    private Long idConsultation;
    private Long idMedecin;
    private Long idPharmacie;
}
