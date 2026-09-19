package com.Mboacare.Mboacare.controller;

import com.Mboacare.Mboacare.dto.MedecinStatsDTO;
import com.Mboacare.Mboacare.services.StatistiquesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatistiquesController {

    private final StatistiquesService statistiquesService;

    @GetMapping("/medecin/{id}")
    public ResponseEntity<MedecinStatsDTO> getStatsMedecin(@PathVariable Long id) {
        return ResponseEntity.ok(statistiquesService.getStatsMedecin(id));
    }
}
