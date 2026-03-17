package com.example.shop;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.Accessoire.TypeAccessoire;
import com.example.shop.Order.Order;
import com.example.shop.Order.OrderRepository;
import com.example.shop.Product.Product;
import com.example.shop.Product.ProductGroupDTO;
import com.example.shop.Product.ProductRepository;
import com.example.shop.Smartphone.Smartphone;
import com.example.shop.User.User;

import org.springframework.data.domain.Pageable;

/**
 * Contrôleur principal pour la page d'accueil et le profil utilisateur.
 * Gère l'affichage des produits, le filtrage et le groupement.
 */
@Controller
public class HomeController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String index(Model model, HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category) {

        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        Pageable pageable = PageRequest.of(page, 8);

        Page<Product> products = Page.empty(pageable);
        List<ProductGroupDTO> productGroups = new ArrayList<>();

        try {
            // Déterminer quelle méthode du repository appeler selon la catégorie et la
            // recherche
            if (category != null && !category.trim().isEmpty()) {
                String cat = category.trim().toLowerCase();

                if (search != null && !search.trim().isEmpty()) {
                    // Catégorie + Recherche
                    products = getProductsByCategoryAndSearch(cat, search.trim(), pageable);
                } else {
                    // Catégorie seule
                    products = getProductsByCategory(cat, pageable);
                }
                model.addAttribute("category", category);
            } else {
                // Pas de filtre de catégorie
                if (search != null && !search.trim().isEmpty()) {
                    products = productRepository.searchProducts(search.trim(), pageable);
                } else {
                    products = productRepository.findAll(pageable);
                }
            }

            if (search != null && !search.trim().isEmpty()) {
                model.addAttribute("search", search);
            }

            // Vérification de nullité
            if (products == null) {
                products = Page.empty(pageable);
            }

            // Grouper les smartphones par nom de modèle de base
            productGroups = groupProductsByModel(products.getContent());

        } catch (Exception e) {
            // En cas d'erreur base de données, logger et utiliser des collections vides
            e.printStackTrace();
            System.err.println("Error loading products: " + e.getMessage());
            products = Page.empty(pageable);
            productGroups = new ArrayList<>();
            model.addAttribute("error", "Unable to load products. Please try again later.");
        }

        model.addAttribute("productGroups", productGroups);
        model.addAttribute("products", products);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());

        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        List<Order> orders = orderRepository.findByUser(user);
        model.addAttribute("orders", orders);
        return "profile";
    }

    private Page<Product> getProductsByCategory(String category, Pageable pageable) {
        switch (category) {
            case "smartphone":
                return productRepository.findSmartphones(pageable);
            case "ecouteurs":
                return productRepository.findAccessoiresByType(TypeAccessoire.ECOUTEURS, pageable);
            case "protections":
                return productRepository.findAccessoiresByType(TypeAccessoire.PROTECTEUR_ECRAN, pageable);
            case "coques":
                return productRepository.findAccessoiresByType(TypeAccessoire.COQUE, pageable);
            case "chargeurs":
                return productRepository.findAccessoiresByType(TypeAccessoire.CHARGEUR, pageable);
            default:
                return productRepository.findAll(pageable);
        }
    }

    private Page<Product> getProductsByCategoryAndSearch(String category, String search, Pageable pageable) {
        switch (category) {
            case "smartphone":
                return productRepository.searchSmartphones(search, pageable);
            case "ecouteurs":
                return productRepository.searchAccessoiresByType(search, TypeAccessoire.ECOUTEURS, pageable);
            case "protections":
                return productRepository.searchAccessoiresByType(search, TypeAccessoire.PROTECTEUR_ECRAN, pageable);
            case "coques":
                return productRepository.searchAccessoiresByType(search, TypeAccessoire.COQUE, pageable);
            case "chargeurs":
                return productRepository.searchAccessoiresByType(search, TypeAccessoire.CHARGEUR, pageable);
            default:
                return productRepository.searchProducts(search, pageable);
        }
    }

    /**
     * Groupe les produits par leur nom de modèle de base.
     * Les smartphones avec le même nom de base sont groupés ensemble.
     * Les accessoires sont gardés comme articles individuels.
     */
    private List<ProductGroupDTO> groupProductsByModel(List<Product> products) {
        Map<String, ProductGroupDTO> groupMap = new LinkedHashMap<>();

        for (Product product : products) {
            // Grouper uniquement les smartphones, garder les accessoires séparés
            if (product instanceof Smartphone) {
                String baseName = ProductGroupDTO.extractBaseModelName(product.getName());

                ProductGroupDTO group = groupMap.get(baseName);
                if (group == null) {
                    group = new ProductGroupDTO(baseName);
                    groupMap.put(baseName, group);
                }
                group.addVariant(product);
            } else {
                // Pour les accessoires, utiliser le nom complet comme nom de base (pas de
                // groupement)
                String fullName = product.getName();
                ProductGroupDTO group = new ProductGroupDTO(fullName);
                group.addVariant(product);
                groupMap.put(fullName + "_" + product.getId(), group); // Utiliser une clé unique
            }
        }

        return new ArrayList<>(groupMap.values());
    }

    @GetMapping("/order/{id}")
    public String orderDetails(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        java.util.Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            // Vérification de sécurité : s'assurer que la commande appartient à
            // l'utilisateur
            if (order.getUser().getId().equals(user.getId())) {
                model.addAttribute("order", order);
                return "order_details";
            }
        }

        return "redirect:/profile";
    }
}
