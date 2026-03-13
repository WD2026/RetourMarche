package com.example.shop.Product;

/**
 * Exception levée lorsqu'il n'y a pas assez de stock pour un produit lors de
 * l'ajout au panier ou de la commande.
 */
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
