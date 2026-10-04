package com.Mboacare.Mboacare.controller;

import com.Mboacare.Mboacare.dto.auth.AuthResponse;
import com.Mboacare.Mboacare.dto.auth.LoginRequest;
import com.Mboacare.Mboacare.dto.auth.RegisterRequest;
import com.Mboacare.Mboacare.entities.Utilisateur;
import com.Mboacare.Mboacare.repositories.UtilisateurRepository;
import com.Mboacare.Mboacare.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Permettre l'accès depuis Angular
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private com.Mboacare.Mboacare.services.NotificationService notificationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Vérifier si l'email existe déjà
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(new AuthResponse("Cet email est déjà utilisé.", null));
        }

        // Créer l'utilisateur
        Utilisateur user = Utilisateur.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .ville(request.getVille())
                .quartier(request.getQuartier())
                .motDePasse(request.getMotDePasse()) // NB: Dans un vrai projet, utiliser BCrypt
                .role(request.getRole())
                .telephone(request.getTelephone())
                .dateNaissance(request.getDateNaissance())
                .genre(request.getGenre())
                .groupeSanguin(request.getGroupeSanguin())
                .poids(request.getPoids())
                .taille(request.getTaille())
                .numeroOrdre(request.getNumeroOrdre())
                .specialite(request.getSpecialite())
                .lieuExercice(request.getLieuExercice())
                .build();

        utilisateurRepository.save(user);

        // Envoyer l'email en temps réel
        emailService.envoyerEmailBienvenue(user.getEmail(), user.getNom(), user.getRole());

        // Envoyer une notification Websocket
        if ("patient".equals(user.getRole())) {
            notificationService.notifyPatient(user.getId(), "Bienvenue", "Votre compte patient a été créé avec succès.");
        } else if ("medecin".equals(user.getRole())) {
            notificationService.notifyMedecin(user.getId(), "Bienvenue", "Votre compte médecin a été créé avec succès.");
        }

        return ResponseEntity.ok(new AuthResponse("Inscription réussie, email envoyé.", user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Utilisateur> optionalUser = utilisateurRepository.findByEmail(request.getEmail());
        
        if (optionalUser.isPresent()) {
            Utilisateur user = optionalUser.get();
            // NB: Dans un vrai projet, vérifier le hash du mot de passe
            if (user.getMotDePasse().equals(request.getMotDePasse())) {
                return ResponseEntity.ok(new AuthResponse("Connexion réussie.", user));
            }
        }
        
        
        return ResponseEntity.status(401).body(new AuthResponse("Email ou mot de passe incorrect.", null));
    }

    // Endpoint pour mettre à jour le profil (incluant la photo)
    @PutMapping("/profil/{id}")
    public ResponseEntity<?> updateProfil(@PathVariable Long id, @RequestBody Utilisateur updatedUser) {
        Optional<Utilisateur> optionalUser = utilisateurRepository.findById(id);
        if (optionalUser.isPresent()) {
            Utilisateur user = optionalUser.get();
            // Mise à jour des champs modifiables
            if (updatedUser.getTelephone() != null) user.setTelephone(updatedUser.getTelephone());
            if (updatedUser.getVille() != null) user.setVille(updatedUser.getVille());
            if (updatedUser.getQuartier() != null) user.setQuartier(updatedUser.getQuartier());
            if (updatedUser.getPoids() != null) user.setPoids(updatedUser.getPoids());
            if (updatedUser.getTaille() != null) user.setTaille(updatedUser.getTaille());
            if (updatedUser.getAllergies() != null) user.setAllergies(updatedUser.getAllergies());
            if (updatedUser.getMaladiesChroniques() != null) user.setMaladiesChroniques(updatedUser.getMaladiesChroniques());
            if (updatedUser.getTraitementsEnCours() != null) user.setTraitementsEnCours(updatedUser.getTraitementsEnCours());
            if (updatedUser.getPhotoProfil() != null) user.setPhotoProfil(updatedUser.getPhotoProfil());
            if (updatedUser.getNumeroOrdre() != null) user.setNumeroOrdre(updatedUser.getNumeroOrdre());
            if (updatedUser.getSpecialite() != null) user.setSpecialite(updatedUser.getSpecialite());
            if (updatedUser.getLieuExercice() != null) user.setLieuExercice(updatedUser.getLieuExercice());

            utilisateurRepository.save(user);
            return ResponseEntity.ok(new AuthResponse("Profil mis à jour avec succès.", user));
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint pour récupérer tous les médecins inscrits (pour la prise de RDV)
    @GetMapping("/medecins")
    public ResponseEntity<List<Utilisateur>> getAllMedecins() {
        List<Utilisateur> medecins = utilisateurRepository.findByRole("medecin");
        return ResponseEntity.ok(medecins);
    }

    // Endpoint pour le mode développeur : Réinitialiser la base de données des utilisateurs
    @DeleteMapping("/reset")
    public ResponseEntity<?> resetAllUsers() {
        utilisateurRepository.deleteAll();
        return ResponseEntity.ok(new AuthResponse("Tous les comptes et profils ont été supprimés avec succès.", null));
    }
}
