package com.Mboacare.Mboacare.repositories;

import com.Mboacare.Mboacare.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByEmail(String email);
    java.util.List<Utilisateur> findByRole(String role);
}
