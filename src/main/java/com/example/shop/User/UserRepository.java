package com.example.shop.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository pour gérer les utilisateurs.
 * Permet de vérifier l'existence d'un email/téléphone et de trouver des
 * utilisateurs.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    boolean existsByTelephone(String telephone);

    User findByEmail(String email);

    Page<User> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom, Pageable pageable);
}