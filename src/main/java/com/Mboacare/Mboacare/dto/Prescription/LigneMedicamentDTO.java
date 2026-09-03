package com.Mboacare.Mboacare.dto.Prescription;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LigneMedicamentDTO {

    @NotBlank(message = "Le nom du medicament est obligatoire")
    private String nomMedicament;

    @NotBlank(message = "La posologie est obligatoire")
    private String posologie;

    @NotBlank(message = "La duree du traitement est obligatoire")
    private String duree;

    @NotNull(message = "La quantite est obligatoire")
    @Positive(message = "La quantite doit etre superieure a 0")
    private Integer quantite;
}