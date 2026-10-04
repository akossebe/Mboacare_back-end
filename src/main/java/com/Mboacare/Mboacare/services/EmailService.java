package com.Mboacare.Mboacare.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void envoyerEmailBienvenue(String toEmail, String nom, String role) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("mboacare.contact@gmail.com"); // À configurer
            message.setTo(toEmail);
            message.setSubject("Bienvenue sur MboaCare !");
            
            String roleText = role.equals("medecin") ? "Médecin" : (role.equals("patient") ? "Patient" : "Pharmacien");
            
            message.setText("Bonjour " + nom + ",\n\n" +
                    "Votre compte " + roleText + " a été créé avec succès sur MboaCare.\n" +
                    "Connectez-vous dès maintenant pour accéder à votre espace personnalisé.\n\n" +
                    "L'équipe MboaCare.");
            
            // mailSender.send(message); // Désactivé pour éviter les erreurs de configuration
            System.out.println("=====================================================");
            System.out.println("SIMULATION D'ENVOI D'EMAIL (Pour la soutenance)");
            System.out.println("Destinataire : " + toEmail);
            System.out.println("Sujet : " + message.getSubject());
            System.out.println("Message :\n" + message.getText());
            System.out.println("=====================================================");
        } catch (Exception e) {
            // Log l'erreur mais on ne bloque pas l'inscription si l'email échoue
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
}
