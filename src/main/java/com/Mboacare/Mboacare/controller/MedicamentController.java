package com.Mboacare.Mboacare.controller;

import com.Mboacare.Mboacare.dto.MedicamentReqdto;
import com.Mboacare.Mboacare.dto.MedicamentResdto;
import com.Mboacare.Mboacare.services.pharmacie.MedicamentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicament")
public class MedicamentController {

    private final MedicamentService medicamentService;

    public MedicamentController(MedicamentService medicamentService) {
        this.medicamentService = medicamentService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createMedicament(@Valid @RequestBody MedicamentReqdto dto) {
        this.medicamentService.addMedicament(dto);
        return ResponseEntity.status(201).body("Médicament créé avec succès !");
    }

    @GetMapping("/get_by_id/{idMedicament}")
    public ResponseEntity<MedicamentResdto> getMedicamentById(@PathVariable String idMedicament) {
        return ResponseEntity.ok(this.medicamentService.getMedicamentById(idMedicament));
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<MedicamentResdto>> getAllMedicament() {
        return ResponseEntity.ok(this.medicamentService.getAllMedicament());
    }

    @PutMapping("/update_by_id/{idMedicament}")
    public ResponseEntity<String> updateMedicament(@PathVariable String idMedicament,
                                                   @Valid @RequestBody MedicamentReqdto dto) {
        this.medicamentService.updateMedicament(idMedicament, dto);
        return ResponseEntity.status(202).body("Médicament modifié avec succès !");
    }

    @DeleteMapping("/delete_by_id/{idMedicament}")
    public ResponseEntity<String> deleteMedicament(@PathVariable String idMedicament) {
        this.medicamentService.deleteMedicament(idMedicament);
        return ResponseEntity.status(202).body("Médicament supprimé avec succès !");
    }

    @GetMapping("/pagination")
    public Page<MedicamentResdto> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "nom") String sortBy
    ) {
        return medicamentService.getPaginated(page, size, sortBy);
    }

    @GetMapping("/forme/{forme}")
    public ResponseEntity<List<MedicamentResdto>> findByForme(@PathVariable String forme) {
        return ResponseEntity.ok(this.medicamentService.findMedicamentByForme(forme));
    }
}
