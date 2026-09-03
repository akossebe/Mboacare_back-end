package com.Mboacare.Mboacare.controller;


import com.Mboacare.Mboacare.dto.Prescription.EnvoyerPharmacieDTO;
import com.Mboacare.Mboacare.dto.Prescription.PrescriptionReqDTO;
import com.Mboacare.Mboacare.dto.Prescription.PrescriptionResDTO;
import com.Mboacare.Mboacare.services.consultation.prescription.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    // POST /api/prescriptions  -> rediger()
    @PostMapping
    public ResponseEntity<PrescriptionResDTO> rediger(@Valid @RequestBody PrescriptionReqDTO dto) {
        PrescriptionResDTO redigee = prescriptionService.rediger(dto);
        return new ResponseEntity<>(redigee, HttpStatus.CREATED);
    }

    // GET /api/prescriptions/5  -> consulter()
    @GetMapping("/{id}")
    public ResponseEntity<PrescriptionResDTO> consulter(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.consulter(id));
    }

    // GET /api/prescriptions
    @GetMapping
    public ResponseEntity<List<PrescriptionResDTO>> getTous() {
        return ResponseEntity.ok(prescriptionService.getTous());
    }

    // DELETE /api/prescriptions/5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        prescriptionService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/prescriptions/5/valider  -> valider()
    @PatchMapping("/{id}/valider")
    public ResponseEntity<PrescriptionResDTO> valider(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.valider(id));
    }

    // PATCH /api/prescriptions/5/envoyer-pharmacie  (body = { "idPharmacie": 2 })  -> envoyerAPharmacie()
    @PatchMapping("/{id}/envoyer-pharmacie")
    public ResponseEntity<PrescriptionResDTO> envoyerAPharmacie(
            @PathVariable Long id, @Valid @RequestBody EnvoyerPharmacieDTO dto) {
        return ResponseEntity.ok(prescriptionService.envoyerAPharmacie(id, dto));
    }

    // PATCH /api/prescriptions/5/delivrer  -> marquerDelivree()
    @PatchMapping("/{id}/delivrer")
    public ResponseEntity<PrescriptionResDTO> marquerDelivree(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.marquerDelivree(id));
    }
}