package com.Mboacare.Mboacare.controller;



import com.Mboacare.Mboacare.dto.Consultation.CompteRenduDTO;
import com.Mboacare.Mboacare.dto.Consultation.ConsultationReqDTO;
import com.Mboacare.Mboacare.dto.Consultation.ConsultationResDTO;
import com.Mboacare.Mboacare.dto.Consultation.DiagnosticReqDTO;
import com.Mboacare.Mboacare.services.consultation.consultation.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    // ---------- CRUD DE BASE ----------
    // Remarque : il n'y a pas de POST "generique" ici car une consultation
    // ne se cree JAMAIS a la main -> elle nait toujours de creerConsultation()
    // a partir d'un rendez-vous confirme (voir plus bas).

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResDTO> getParId(@PathVariable Long id) {
        return ResponseEntity.ok(consultationService.getParId(id));
    }

    @GetMapping
    public ResponseEntity<List<ConsultationResDTO>> getTous() {
        return ResponseEntity.ok(consultationService.getTous());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        consultationService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    // ---------- ACTIONS METIER ----------

    // POST /api/consultations  (body = { "idRendezVous": 3, "motif": "..." })
    @PostMapping
    public ResponseEntity<ConsultationResDTO> creer(@Valid @RequestBody ConsultationReqDTO dto) {
        ConsultationResDTO creee = consultationService.creerConsultation(dto);
        return new ResponseEntity<>(creee, HttpStatus.CREATED);
    }

    // PATCH /api/consultations/5/diagnostic  (body = { "diagnostic": "...", "observations": "..." })
    @PatchMapping("/{id}/diagnostic")
    public ResponseEntity<ConsultationResDTO> enregistrerDiagnostic(
            @PathVariable Long id, @Valid @RequestBody DiagnosticReqDTO dto) {
        return ResponseEntity.ok(consultationService.enregistrerDiagnostic(id, dto));
    }

    // PATCH /api/consultations/5/cloturer
    @PatchMapping("/{id}/cloturer")
    public ResponseEntity<ConsultationResDTO> cloturer(@PathVariable Long id) {
        return ResponseEntity.ok(consultationService.cloturerConsultation(id));
    }

    // GET /api/consultations/5/compte-rendu
    @GetMapping("/{id}/compte-rendu")
    public ResponseEntity<CompteRenduDTO> compteRendu(@PathVariable Long id) {
        return ResponseEntity.ok(consultationService.genererCompteRendu(id));
    }
}