package com.example.shop.Smartphone;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour gérer les opérations de base de données sur les smartphones.
 * Permet de rechercher des smartphones par nom ou marque.
 */
@Repository
public interface SmartphoneRepository extends JpaRepository<Smartphone, Long> {
    Page<Smartphone> findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(String name, String brand,
            Pageable pageable);
}