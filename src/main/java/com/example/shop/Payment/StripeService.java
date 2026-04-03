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
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .setCustomerEmail(user.getEmail());

        for (Cart item : cartItems) {
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity((long) item.getQuantity())
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency(currency)
                                            .setUnitAmount((long) (item.getProduct().getPrice() * 100)) // Stripe uses cents
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(item.getProduct().getName())
                                                            .build()
                                            )
                                            .build()
                            )
                            .build()
            );
        }

        // Add trade-in discount if applicable as a negative line item is not allowed in checkout sessions
        // Instead, we should ideally use a coupon or just subtract from the total.
        // For simplicity in sandbox, if there's a discount, we can adjust the unit amount of items or add a "Discount" item if positive.
        // Note: Stripe Checkout doesn't easily support negative amounts directly in line items.
        
        // If we want to be precise with the total price calculated in CartController:
        // Let's just create a single line item for the "Order Total" to ensure it matches exactly what was shown to the user.
        
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
}
