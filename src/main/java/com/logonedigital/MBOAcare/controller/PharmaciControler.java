package com.logonedigital.MBOAcare.controller;

import com.logonedigital.MBOAcare.dto.PharmaciReqdto;
import com.logonedigital.MBOAcare.dto.PharmaciResdto;
import com.logonedigital.MBOAcare.service.pharmaci.PharmaciService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pharmaci")
public class PharmaciControler {

    private final PharmaciService pharmaciService;

    public PharmaciControler(PharmaciService pharmaciService) {
        this.pharmaciService = pharmaciService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPharmaci(@Valid @RequestBody PharmaciReqdto pharmaciReqdto) {
        this.pharmaciService.addPharmaci(pharmaciReqdto);
        return ResponseEntity.status(201).body("Pharmacie cree avec succes !");
    }

    @GetMapping("/get_by_id/{idPharmaci}")
    public ResponseEntity<PharmaciResdto> getPharmaciById(@PathVariable String idPharmaci) {
        return ResponseEntity.status(200).body(this.pharmaciService.getPharmaciById(idPharmaci));
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<PharmaciResdto>> getAllPharmaci() {
        return ResponseEntity.status(200).body(this.pharmaciService.getAllPharmaci());
    }

    @PutMapping("/update_by_id/{idPharmaci}")
    public ResponseEntity<String> updatePharmaci(@PathVariable String idPharmaci, @Valid @RequestBody PharmaciReqdto pharmaciReqdto) {
        this.pharmaciService.updatePharmaci(idPharmaci, pharmaciReqdto);
        return ResponseEntity.status(202).body("Pharmacie modifiee avec succes !");
    }

    @DeleteMapping("/delete_by_id/{idPharmaci}")
    public ResponseEntity<String> deletePharmaci(@PathVariable String idPharmaci) {
        this.pharmaciService.deletePharmaci(idPharmaci);
        return ResponseEntity.status(202).body("Pharmacie supprimee avec succes !");
    }

    @GetMapping("/pagination")
    public Page<PharmaciResdto> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "nom") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return pharmaciService.getPaginated(page, size, sortBy, direction);
    }
}
