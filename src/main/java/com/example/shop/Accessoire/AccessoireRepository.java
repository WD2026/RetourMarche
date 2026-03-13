package com.example.shop.Accessoire;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shop.Smartphone.Smartphone;

/**
 * Repository pour gérer les opérations de base de données sur les accessoires.
 * Permet de trouver des accessoires par compatibilité, nom ou type.
 */
@Repository
public interface AccessoireRepository extends JpaRepository<Accessoire, Long> {

    List<Accessoire> findBySmartphonesContaining(Smartphone smartphone);

    Page<Accessoire> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Accessoire> findByType(TypeAccessoire type, Pageable pageable);

}