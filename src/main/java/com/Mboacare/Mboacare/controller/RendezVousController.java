package com.Mboacare.Mboacare.controller;
import com.Mboacare.Mboacare.dto.RendezVous.RendezVousReqDTO;
import com.Mboacare.Mboacare.dto.RendezVous.RendezVousResDTO;
import com.Mboacare.Mboacare.dto.RendezVous.ReporterRendezVousDTO;
import com.Mboacare.Mboacare.services.consultation.RendezVous.RendezVousService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@RestController
@RequestMapping("/api/rendez-vous")
@RequiredArgsConstructor
public class RendezVousController {
    private final RendezVousService rendezVousService;
    @PostMapping
    public ResponseEntity<RendezVousResDTO> creer(@Valid @RequestBody RendezVousReqDTO dto) {
        RendezVousResDTO cree = rendezVousService.prendreRendezVous(dto);
        return new ResponseEntity<>(cree, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RendezVousResDTO> getParId(@PathVariable Long id) {
        return ResponseEntity.ok(rendezVousService.getParId(id));
    }
    @GetMapping
    public ResponseEntity<Page<RendezVousResDTO>> getTous(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateSouhaitee") String trier,
            @RequestParam(required = false) Long idPatient,
            @RequestParam(required = false) Long idMedecin) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(trier).descending()
        );
        return ResponseEntity.ok(
                rendezVousService.getTous(pageable, idPatient, idMedecin)
        );
    }
    @GetMapping("/creneaux-occupes")
    public ResponseEntity<List<LocalTime>> getCreneauxOccupes(
            @RequestParam Long idMedecin,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(rendezVousService.getCreneauxOccupes(idMedecin, date));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        rendezVousService.supprimer(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<RendezVousResDTO> confirmer(@PathVariable Long id) {
        return ResponseEntity.ok(rendezVousService.confirmerRendezVous(id));
    }
    @PatchMapping("/{id}/reporter")
    public ResponseEntity<RendezVousResDTO> reporter(
            @PathVariable Long id, @Valid @RequestBody ReporterRendezVousDTO dto) {
        return ResponseEntity.ok(rendezVousService.reporterRendezVous(id, dto));
    }
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<RendezVousResDTO> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(rendezVousService.annulerRendezVous(id));
    }
}
