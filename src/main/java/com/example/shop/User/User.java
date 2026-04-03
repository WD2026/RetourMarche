package com.example.shop.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Représente un utilisateur de l'application (Client ou Administrateur).
 * Stocke les informations personnelles et les identifiants de connexion.
 */
@Data
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @jakarta.validation.constraints.NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @jakarta.validation.constraints.NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false)
    private String prenom;

    @jakarta.validation.constraints.NotBlank(message = "L'email est obligatoire")
    @jakarta.validation.constraints.Email(message = "L'email doit être valide")
    @Column(nullable = false, unique = true)
    private String email;

    @jakarta.validation.constraints.NotBlank(message = "Le mot de passe est obligatoire")
    @jakarta.validation.constraints.Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @Column(nullable = false)
    private String password;

    @jakarta.validation.constraints.NotBlank(message = "Le téléphone est obligatoire")
    @jakarta.validation.constraints.Pattern(regexp = "^(\\+33|0)[1-9](\\s?\\d{2}){4}$", message = "Format de téléphone invalide")
    @Column(nullable = false, unique = true)
    private String telephone;

    @Column(nullable = false)
    private String role = "USER";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
