package com.example.shop.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe abstraite représentant un produit générique (Smartphone ou
 * Accessoire).
 * Contient les propriétés communes : nom, prix, image, description, stock.
 * Utilise la stratégie JOINED pour l'héritage en base de données.
 */
@Data
@Entity
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    @Column(length = 2048)
    private String imageUrl;
    @Column(length = 2048)
    private String description;

    @Column
    private Integer stock = 0;


}
