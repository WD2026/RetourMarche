package com.example.shop.Cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.shop.Product.Product;
import com.example.shop.User.User;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour gérer les paniers des utilisateurs.
 * Permet de récupérer, modifier ou supprimer les articles du panier.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUser(User user);

    Optional<Cart> findByUserAndProduct(User user, Product product);

    void deleteByUser(User user);
}
