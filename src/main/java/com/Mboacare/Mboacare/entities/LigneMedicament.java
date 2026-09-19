package com.Mboacare.Mboacare.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class LigneMedicament {


    private String nomMedicament;


    private String posologie;


    private String duree;


    private Integer quantite;
}
