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
    @jakarta.validation.constraints.NotBlank(message = "Le prénom est obligatoire")
    private String firstName;

    @jakarta.validation.constraints.NotBlank(message = "Le nom est obligatoire")
    private String lastName;

    @jakarta.validation.constraints.NotBlank(message = "L'adresse est obligatoire")
    private String address;

    @jakarta.validation.constraints.NotBlank(message = "La ville est obligatoire")
    private String city;

    @jakarta.validation.constraints.NotBlank(message = "Le code postal est obligatoire")
    @jakarta.validation.constraints.Pattern(regexp = "^[0-9]{5}$", message = "Le code postal doit contenir 5 chiffres")
    private String zip;

    @jakarta.validation.constraints.NotBlank(message = "Le pays est obligatoire")
    private String country;

    // Moyen de paiement
    private String paymentMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private com.example.shop.Payment.Payment payment;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
