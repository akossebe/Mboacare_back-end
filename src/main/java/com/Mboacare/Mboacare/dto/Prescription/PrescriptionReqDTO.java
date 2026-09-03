package com.Mboacare.Mboacare.dto.Prescription;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DTO pour REDIGER une prescription (methode rediger()).
 * @Valid sur la liste + @NotEmpty garantissent qu'on ne peut pas
 * creer une prescription sans au moins un medicament dedans.
 */
@Getter
@Setter
public class PrescriptionReqDTO {

    @NotNull(message = "L'identifiant de la consultation est obligatoire")
    private Long idConsultation;

    @NotEmpty(message = "La prescription doit contenir au moins un medicament")
    @Valid
    private List<LigneMedicamentDTO> lignesMedicaments;
}
