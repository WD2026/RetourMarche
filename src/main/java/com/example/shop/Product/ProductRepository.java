package com.example.shop.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.shop.Accessoire.TypeAccessoire;

/**
 * Repository pour gérer les produits (Smartphones et Accessoires).
 * Contient des méthodes de recherche et de filtrage avancées.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Product> searchProducts(@Param("search") String search, Pageable pageable);

    // Filtrer par type Smartphone
    @Query("SELECT s FROM Smartphone s")
    Page<Product> findSmartphones(Pageable pageable);

    @Query("SELECT s FROM Smartphone s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Product> searchSmartphones(@Param("search") String search, Pageable pageable);

    // Filtrer par type Accessoire
    @Query("SELECT a FROM Accessoire a WHERE a.type = :type")
    Page<Product> findAccessoiresByType(@Param("type") TypeAccessoire type, Pageable pageable);

    @Query("SELECT a FROM Accessoire a WHERE a.type = :type AND (LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> searchAccessoiresByType(@Param("search") String search, @Param("type") TypeAccessoire type,
            Pageable pageable);
}
