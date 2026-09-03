package com.Mboacare.Mboacare.dto.Prescription;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** DTO utilise pour l'action envoyerAPharmacie() : on precise juste la pharmacie choisie. */
@Getter
@Setter
public class EnvoyerPharmacieDTO {

    @NotNull(message = "L'identifiant de la pharmacie est obligatoire")
    private Long idPharmacie;
}