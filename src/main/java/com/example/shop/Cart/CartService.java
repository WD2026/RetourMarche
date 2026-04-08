package com.example.shop.Cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shop.Order.Order;
import com.example.shop.Order.OrderItem;
import com.example.shop.Order.OrderRepository;
import com.example.shop.Product.InsufficientStockException;
import com.example.shop.Product.Product;
import com.example.shop.Product.ProductRepository;
import com.example.shop.Smartphone.Smartphone;
import com.example.shop.User.User;

import java.util.List;
import java.util.Optional;

/**
 * Service gérant la logique métier du panier (ajout, suppression, calcul total,
 * création de commande).
 */
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Order createOrder(User user, String firstName, String lastName, String address, String city, String zip,
            String country, String paymentMethod, double finalTotal, String status) {
        List<Cart> cartItems = getItems(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(java.time.LocalDateTime.now());
        order.setStatus(status);
        order.setFirstName(firstName);
        order.setLastName(lastName);
        order.setAddress(address);
        order.setCity(city);
        order.setZip(zip);
        order.setCountry(country);
        order.setPaymentMethod(paymentMethod);

        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            // Vérifier le stock
            if (product.getStock() < quantity) {
                throw new InsufficientStockException("Stock insuffisant pour le produit : " + product.getName());
            }

            // Diminuer le stock
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(
                    product,
                    quantity,
                    product.getPrice(),
                    cartItem.getInsuranceType());
            order.addItem(orderItem);

        }
        order.setTotalPrice(finalTotal);

        orderRepository.save(order);
        // clearCart(user); // Will be cleared after successful payment or confirmation

        return order;
    }

    /**
     * Ajoute un produit au panier de l'utilisateur.
     * Si le produit existe déjà, on augmente la quantité (+1).
     * Valide que la quantité totale ne dépasse pas le stock disponible.
     */
    public void addProduct(User user, Product product, int quantity) {
        Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);

        int currentQuantity = existingCart.map(Cart::getQuantity).orElse(0);
        int newQuantity = currentQuantity + quantity;

        if (newQuantity > product.getStock()) {
            throw new InsufficientStockException(
                    "Stock insuffisant. Seulement " + product.getStock() + " articles disponibles.");
        }

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.setQuantity(newQuantity);
            cartRepository.save(cart);
        } else {
            Cart cart = new Cart(user, product, quantity);
            cartRepository.save(cart);
        }
    }

    /**
     * Supprime complètement une ligne du panier.
     */
    public void removeProduct(User user, Product product) {
        Optional<Cart> cart = cartRepository.findByUserAndProduct(user, product);
        cart.ifPresent(cartRepository::delete);
    }

    /**
     * Diminue la quantité d'un produit (-1).
     * Si la quantité tombe à 0, on supprime le produit.
     */
    public void decreaseQuantity(User user, Product product) {
        Optional<Cart> existingCart = cartRepository.findByUserAndProduct(user, product);

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            if (cart.getQuantity() > 1) {
                cart.setQuantity(cart.getQuantity() - 1);
                cartRepository.save(cart);
            } else {
                cartRepository.delete(cart);
            }
        }
    }

    /**
     * Renvoie la liste des items du panier pour l'utilisateur.
     */
    public List<Cart> getItems(User user) {
        return cartRepository.findByUser(user);
    }

    /**
     * Calcule le prix total du panier (produits + assurances).
     */
    public double getTotal(User user) {
        return getItems(user).stream()
                .mapToDouble(cart -> {
                    double productPrice = cart.getProduct().getPrice() * cart.getQuantity();
                    double insurancePrice = (cart.getInsuranceType() != null) ? cart.getInsuranceType().getPrice()
                            : 0.0;
                    return productPrice + insurancePrice;
                })
                .sum();
    }

    /**
     * Met à jour l'assurance d'un item du panier.
     */
    public void updateInsurance(User user, Long productId, InsuranceType insuranceType) {
        Optional<Cart> cartOpt = cartRepository.findByUserAndProduct(user,
                productRepository.findById(productId).orElse(null));
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            // Vérifier si c'est un smartphone avant d'appliquer l'assurance
            if (cart.getProduct() instanceof Smartphone) {
                cart.setInsuranceType(insuranceType);
                cartRepository.save(cart);
            }
        }
    }

    /**
     * Compte le nombre total d'articles.
     */
    public int getItemCount(User user) {
        return getItems(user).stream()
                .mapToInt(Cart::getQuantity)
                .sum();
    }

    /**
     * Vide le panier (après une commande).
     */
    @Transactional
    public void clearCart(User user) {
        cartRepository.deleteByUser(user);
    }
}