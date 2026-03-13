package com.example.shop.Smartphone;

import java.util.List;

import com.example.shop.Accessoire.Accessoire;
import com.example.shop.Product.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Représente un smartphone, héritant de Product.
 * Contient des attributs spécifiques comme la marque, la couleur, le stockage
 * et l'état.
 */
@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Smartphone extends Product {

    private String brand;
    private String color;
    private Integer storageCapacity;
    private String condition;

    private Double newPrice;

    @ManyToMany(mappedBy = "smartphones")
    private List<Accessoire> accessoires;

}
