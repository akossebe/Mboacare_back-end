package com.Mboacare.Mboacare.dto.Consultation;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiagnosticReqDTO {

    @NotBlank(message = "Le diagnostic ne peut pas etre vide")
    private String diagnostic;

    private String observations;
}

