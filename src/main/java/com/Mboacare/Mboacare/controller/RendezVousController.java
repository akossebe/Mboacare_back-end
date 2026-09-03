package com.Mboacare.Mboacare.controller;

import com.Mboacare.Mboacare.dto.RendezVous.RendezVousReqDTO;
import com.Mboacare.Mboacare.dto.RendezVous.RendezVousResDTO;

import com.Mboacare.Mboacare.dto.RendezVous.ReporterRendezVousDTO;
import com.Mboacare.Mboacare.services.consultation.RendezVous.RendezVousService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @RestController = @Controller + @ResponseBody : Spring convertit
 * automatiquement les objets Java qu'on renvoie (DTO) en JSON.
 *
 * @RequestMapping("/api/rendez-vous") = prefixe commun a toutes les
 * routes de ce controller. Exemple : GET /api/rendez-vous/5
 */
@RestController
@RequestMapping("/api/rendez-vous")
@RequiredArgsConstructor
public class RendezVousController {

    private final RendezVousService rendezVousService;

    // ---------- CRUD DE BASE ----------

    // POST /api/rendez-vous  (body JSON = RendezVousRequestDTO)
    // @Valid declenche automatiquement la validation (@NotNull, @Future, ...)
    @PostMapping
    public ResponseEntity<RendezVousResDTO> creer(@Valid @RequestBody RendezVousReqDTO dto) {
        RendezVousResDTO cree = rendezVousService.prendreRendezVous(dto);
        // HttpStatus.CREATED = code 201, standard pour une creation reussie
        return new ResponseEntity<>(cree, HttpStatus.CREATED);
    }

    // GET /api/rendez-vous/5
    @GetMapping("/{id}")
    public ResponseEntity<RendezVousResDTO> getParId(@PathVariable Long id) {
        return ResponseEntity.ok(rendezVousService.getParId(id));
    }

    // GET /api/rendez-vous
    @GetMapping
    public ResponseEntity<List<RendezVousResDTO>> getTous() {
        return ResponseEntity.ok(rendezVousService.getTous());
    }

    // DELETE /api/rendez-vous/5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        rendezVousService.supprimer(id);
        // HttpStatus.NO_CONTENT = code 204 : suppression reussie, rien a renvoyer
        return ResponseEntity.noContent().build();
    }

    // ---------- ACTIONS METIER (routes specifiques) ----------

    // PATCH /api/rendez-vous/5/confirmer
    // On utilise PATCH (et non PUT) car on ne modifie qu'UNE partie de la ressource (le statut)
    @PatchMapping("/{id}/confirmer")
    public ResponseEntity<RendezVousResDTO> confirmer(@PathVariable Long id) {
        return ResponseEntity.ok(rendezVousService.confirmerRendezVous(id));
    }

    // PATCH /api/rendez-vous/5/reporter  (body JSON = ReporterRendezVousDTO)
    @PatchMapping("/{id}/reporter")
    public ResponseEntity<RendezVousResDTO> reporter(
            @PathVariable Long id, @Valid @RequestBody ReporterRendezVousDTO dto) {
        return ResponseEntity.ok(rendezVousService.reporterRendezVous(id, dto));
    }

    // PATCH /api/rendez-vous/5/annuler
    @PatchMapping("/{id}/annuler")
    public ResponseEntity<RendezVousResDTO> annuler(@PathVariable Long id) {
        return ResponseEntity.ok(rendezVousService.annulerRendezVous(id));
    }
}
