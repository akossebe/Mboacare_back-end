package com.Mboacare.Mboacare.enums;


    /**
     * Un "enum" (enumeration) sert a limiter une valeur a une liste fixe
     * de choix possibles. Ici, le statut d'un rendez-vous ne peut JAMAIS
     * etre autre chose que ces 4 valeurs -> ca evite les fautes de frappe
     * du style statut = "Anullé" au lieu de "Annule".
     */
    public enum StatutRendezVous {
        EN_ATTENTE,   // le patient vient de demander le rendez-vous
        CONFIRME,     // le medecin a confirme le creneau
        ANNULE,       // le rendez-vous a ete annule (par le patient ou le medecin)
        HONORE        // le patient s'est presente, une consultation a ete creee
    }


