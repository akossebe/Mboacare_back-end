package com.Mboacare.Mboacare.dto.auth;
import com.Mboacare.Mboacare.entities.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private Utilisateur utilisateur;
}
