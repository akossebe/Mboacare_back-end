package com.Mboacare.Mboacare.exception;

    /**
     * On leve cette exception quand une action est refusee pour une raison
     * "metier" (pas technique). Exemples concrets tires du cahier des charges :
     *   - vouloir confirmer un rendez-vous deja ANNULE
     *   - vouloir cloturer une consultation sans avoir rempli le diagnostic
     *   - vouloir rediger une prescription sur une consultation pas encore CLOTUREE
     * -> le client recevra une reponse HTTP 400 (Bad Request) avec le message
     * explicatif exact.
     */
    public class BusinessRuleException extends RuntimeException {
        public BusinessRuleException(String message) {
            super(message);
        }
    }


