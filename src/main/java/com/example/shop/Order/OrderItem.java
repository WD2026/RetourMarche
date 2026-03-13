package com.example.shop.Order;

import com.example.shop.Cart.InsuranceType;
import com.example.shop.Product.Product;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente un article dans une commande (ligne de commande).
 * Stocke le produit, la quantité, le prix d'achat et l'assurance choisie.
 */
@Data
@Entity
@Table(name = "order_items")
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;

    private Double price; // Prix unitaire au moment de l'achat

    @Enumerated(EnumType.STRING)
    private InsuranceType insuranceType;

    public OrderItem(Product product, Integer quantity, Double price, InsuranceType insuranceType) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.insuranceType = insuranceType;
    }
}
