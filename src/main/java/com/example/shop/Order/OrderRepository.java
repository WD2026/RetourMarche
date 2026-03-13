package com.example.shop.Order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shop.User.User;

import java.util.List;

/**
 * Repository pour gérer les commandes.
 * Permet de récupérer l'historique des commandes d'un utilisateur.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}
