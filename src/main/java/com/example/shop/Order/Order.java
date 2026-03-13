package com.example.shop.Order;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.shop.User.User;

/**
 * Représente une commande passée par un utilisateur.
 * Contient les informations de livraison, le paiement et la liste des articles
 * commandés.
 */
@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime orderDate;

    private Double totalPrice;

    private String status; // ex: "CONFIRMED"

    // Adresse de livraison
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String country;

    // Moyen de paiement
    private String paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
