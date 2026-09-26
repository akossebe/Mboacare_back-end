package com.Mboacare.Mboacare.controller;

import com.Mboacare.Mboacare.dto.PharmaciReqdto;
import com.Mboacare.Mboacare.dto.PharmaciResdto;
import com.Mboacare.Mboacare.services.pharmacie.PharmaciService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacies")
public class PharmaciController {
    private final PharmaciService pharmaciService;

    public PharmaciController(PharmaciService pharmaciService) {
        this.pharmaciService = pharmaciService;
    }

    @PostMapping
    public ResponseEntity<String> addPharmaci(@Valid @RequestBody PharmaciReqdto pharmaciReqdto) {
        this.pharmaciService.addPharmaci(pharmaciReqdto);
        return ResponseEntity.status(201).body("Pharmacie créée avec succès !");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PharmaciResdto> getPharmaciById(@PathVariable String id) {
        return ResponseEntity.ok(this.pharmaciService.getPharmaciById(id));
    }

    @GetMapping
    public ResponseEntity<List<PharmaciResdto>> getAllPharmaci() {
        return ResponseEntity.ok(this.pharmaciService.getAllPharmaci());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePharmaci(@PathVariable String id, @Valid @RequestBody PharmaciReqdto pharmaciReqdto) {
        this.pharmaciService.updatePharmaci(id, pharmaciReqdto);
        return ResponseEntity.status(202).body("Pharmacie modifiée avec succès !");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePharmaci(@PathVariable String id) {
        this.pharmaciService.deletePharmaci(id);
        return ResponseEntity.status(202).body("Pharmacie supprimée avec succès !");
    }

    @GetMapping("/pagination")
    public Page<PharmaciResdto> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return pharmaciService.getPaginated(page, size, sortBy, direction);
    }
}
