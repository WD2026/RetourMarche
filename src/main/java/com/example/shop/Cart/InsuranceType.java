package com.example.shop.Cart;

/**
 * Type d'assurance disponible pour les produits (Smartphones).
 * Chaque type a un prix et un libellé.
 */
public enum InsuranceType {
    NONE(0.0, "Pas d'assurance"),
    LIGHT(40.99, "Assurance Light"),
    PREMIUM(80.99, "Assurance Premium");

    private final double price;
    private final String label;

    InsuranceType(double price, String label) {
        this.price = price;
        this.label = label;
    }

    public double getPrice() {
        return price;
    }

    public String getLabel() {
        return label;
    }
}
