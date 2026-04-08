package com.example.shop.Payment;

import com.example.shop.Cart.Cart;
import com.example.shop.User.User;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @Value("${stripe.currency}")
    private String currency;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public Session createCheckoutSession(User user, List<Cart> cartItems, double totalPrice, String successUrl, String cancelUrl) throws Exception {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .setCustomerEmail(user.getEmail())
                .addLineItem(
                    SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(currency)
                                .setUnitAmount((long) (totalPrice * 100))
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName("Commande RetourMarché")
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .build();

        return Session.create(params);
    }

    public Session retrieveSession(String sessionId) throws Exception {
        return Session.retrieve(sessionId);
    }
}
