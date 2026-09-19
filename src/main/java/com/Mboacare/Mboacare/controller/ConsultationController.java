package com.Mboacare.Mboacare.controller;



import com.Mboacare.Mboacare.dto.Consultation.CompteRenduDTO;
import com.Mboacare.Mboacare.dto.Consultation.ConsultationReqDTO;
import com.Mboacare.Mboacare.dto.Consultation.ConsultationResDTO;
import com.Mboacare.Mboacare.dto.Consultation.DiagnosticReqDTO;
import com.Mboacare.Mboacare.entities.Consultation;
import com.Mboacare.Mboacare.exception.ResourceNotFoundException;
import com.Mboacare.Mboacare.repositories.ConsultationRepository;
import com.Mboacare.Mboacare.services.PdfService;
import com.Mboacare.Mboacare.services.consultation.consultation.ConsultationService;
import com.lowagie.text.DocumentException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;
    private final PdfService pdfService;
    private final ConsultationRepository consultationRepository;

    // ... (autres méthodes inchangées)

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> genererPdf(@PathVariable Long id) throws DocumentException {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation introuvable"));
        byte[] pdf = pdfService.genererPdfConsultation(consultation);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "consultation_" + id + ".pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    // ---------- CRUD DE BASE ----------

    @GetMapping("/{id}")
    public ResponseEntity<ConsultationResDTO> getParId(@PathVariable Long id) {
        return ResponseEntity.ok(consultationService.getParId(id));
    }

    @GetMapping
    public ResponseEntity<Page<ConsultationResDTO>> getTous(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateConsultation") String trier) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(trier).descending());
        return ResponseEntity.ok(consultationService.getTous(pageable));
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