package com.example.shop.Cart;

import com.example.shop.Product.Product;
import com.example.shop.User.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente un article dans le panier d'un utilisateur.
 * Lie un utilisateur à un produit avec une quantité et une assurance
 * optionnelle.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    @Enumerated(EnumType.STRING)
    private InsuranceType insuranceType = InsuranceType.NONE;

    /**
     * Custom constructor for creating a cart item with user, product, and quantity.
     * The id will be auto-generated and insuranceType defaults to NONE.
     */
    public Cart(User user, Product product, int quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.insuranceType = InsuranceType.NONE;
    }

}
