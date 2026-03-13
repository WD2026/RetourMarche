package com.example.shop.Accessoire;

import java.util.List;

import com.example.shop.Product.Product;
import com.example.shop.Smartphone.Smartphone;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Représente un accessoire (coque, chargeur, etc.) qui hérite de Product.
 * Contient le type d'accessoire et la liste des smartphones compatibles.
 */
@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Accessoire extends Product {

    @Enumerated(EnumType.STRING)
    private TypeAccessoire type;

    @ManyToMany
    private List<Smartphone> smartphones;

}