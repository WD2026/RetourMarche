package com.example.shop.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;

/**
 * DTO pour regrouper les variantes d'un même produit (ex: iPhone 13 en
 * différentes couleurs/stockages).
 * Permet d'afficher une seule carte produit sur la page d'accueil avec une
 * plage de prix.
 */
@Data
public class ProductGroupDTO {
    private String baseModelName;
    private List<Product> variants;
    private Double minPrice;
    private Double maxPrice;
    private String imageUrl;
    private String description;

    public ProductGroupDTO(String baseModelName) {
        this.baseModelName = baseModelName;
        this.variants = new ArrayList<>();
    }

    public void addVariant(Product product) {
        variants.add(product);

        // Mettre à jour les prix min/max
        if (minPrice == null || product.getPrice() < minPrice) {
            minPrice = product.getPrice();
        }
        if (maxPrice == null || product.getPrice() > maxPrice) {
            maxPrice = product.getPrice();
        }

        // Utiliser l'image et la description de la première variante
        if (imageUrl == null) {
            imageUrl = product.getImageUrl();
            description = product.getDescription();
        }
    }

    /**
     * Extrait le nom du modèle de base à partir du nom du produit.
     * Exemples:
     * "iPhone 17 Pro 128GB Noir" -> "iPhone 17 Pro"
     * "Samsung Galaxy S24 256GB Blanc" -> "Samsung Galaxy S24"
     * "AirPods Pro 2" -> "AirPods Pro 2" (pas de stockage/couleur)
     */
    public static String extractBaseModelName(String productName) {
        if (productName == null) {
            return "";
        }

        // Pattern pour supprimer la capacité de stockage (ex: "128GB", "256 GB", "1TB")
        // et les mots de couleur courants à la fin
        Pattern pattern = Pattern.compile(
                "^(.+?)\\s*(?:\\d+\\s*(?:GB|TB|Go|To))?\\s*(?:Noir|Blanc|Bleu|Rouge|Vert|Rose|Gris|Or|Argent|Violet|Jaune|Orange|Black|White|Blue|Red|Green|Pink|Gray|Gold|Silver|Purple|Yellow|Orange)?\\s*$",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(productName.trim());
        if (matcher.matches()) {
            return matcher.group(1).trim();
        }

        return productName.trim();
    }

    public int getVariantCount() {
        return variants.size();
    }

    public boolean hasMultipleVariants() {
        return variants.size() > 1;
    }

    public String getPriceRange() {
        if (minPrice == null) {
            return "0 €";
        }
        if (minPrice.equals(maxPrice)) {
            return String.format("%.2f €", minPrice);
        }
        return String.format("%.2f € - %.2f €", minPrice, maxPrice);
    }
}
