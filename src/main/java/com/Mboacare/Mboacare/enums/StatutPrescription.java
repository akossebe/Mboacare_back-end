package com.Mboacare.Mboacare.enums;


    public enum StatutPrescription {
        EMISE,       // le medecin vient de rediger la prescription
        TRANSMISE,   // la prescription a ete envoyee a une pharmacie
        DELIVREE,    // le pharmacien a delivre les medicaments
        EXPIREE      // la prescription n'est plus valable (delai depasse)
    }


