package com.Mboacare.Mboacare.controller;
import com.Mboacare.Mboacare.dto.Prescription.EnvoyerPharmacieDTO;
import com.Mboacare.Mboacare.dto.Prescription.PrescriptionReqDTO;
import com.Mboacare.Mboacare.dto.Prescription.PrescriptionResDTO;
import com.Mboacare.Mboacare.services.consultation.prescription.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {
    private final PrescriptionService prescriptionService;
    @PostMapping
    public ResponseEntity<PrescriptionResDTO> rediger(@Valid @RequestBody PrescriptionReqDTO dto) {
        PrescriptionResDTO redigee = prescriptionService.rediger(dto);
        return new ResponseEntity<>(redigee, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResDTO> consulter(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.consulter(id));
    }
    @GetMapping
    public ResponseEntity<Page<PrescriptionResDTO>> getTous(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateEmission") String trier,
            @RequestParam(required = false) Long idPatient,
            @RequestParam(required = false) Long idMedecin) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(trier).descending());
        return ResponseEntity.ok(prescriptionService.getTous(pageable, idPatient, idMedecin));
    }
    @GetMapping("/consultation/{idConsultation}")
    public ResponseEntity<PrescriptionResDTO> getParConsultation(@PathVariable Long idConsultation) {
        return ResponseEntity.ok(prescriptionService.getParConsultation(idConsultation));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        prescriptionService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/valider")
    public ResponseEntity<PrescriptionResDTO> valider(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.valider(id));
    }
    @PatchMapping("/{id}/envoyer-pharmacie")
    public ResponseEntity<PrescriptionResDTO> envoyerAPharmacie(
            @PathVariable Long id, @Valid @RequestBody EnvoyerPharmacieDTO dto) {
        return ResponseEntity.ok(prescriptionService.envoyerAPharmacie(id, dto));
    }
    @PatchMapping("/{id}/delivrer")
    public ResponseEntity<PrescriptionResDTO> marquerDelivree(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.marquerDelivree(id));
    }
}
