package com.example.shop.Payment;

import com.example.shop.Order.Order;
import com.example.shop.Order.OrderRepository;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    @Value("${stripe.webhook.secret:}")
    private String endpointSecret;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;

        try {
            if (endpointSecret != null && !endpointSecret.isEmpty()) {
                event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            } else {
                // In development without a secret, we might skip signature verification
                // NOT RECOMMENDED FOR PRODUCTION
                event = com.stripe.net.ApiResource.GSON.fromJson(payload, Event.class);
            }
        } catch (Exception e) {
            logger.error("Error verifying webhook signature", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Webhook Error");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().get();
            handleCheckoutSessionCompleted(session);
        }

        return ResponseEntity.ok("Success");
    }

    private void handleCheckoutSessionCompleted(Session session) {
        String sessionId = session.getId();
        String paymentIntentId = session.getPaymentIntent();
        
        java.util.Optional<Payment> paymentOpt = paymentRepository.findByStripeSessionId(sessionId);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setStripePaymentIntentId(paymentIntentId);
            payment.setStatus("SUCCEEDED");
            paymentRepository.save(payment);
            
            Order order = payment.getOrder();
            order.setStatus("PAID");
            orderRepository.save(order);
            
            logger.info("Order {} successfully paid via webhook (Session ID: {})", order.getId(), sessionId);
        } else {
            logger.warn("Received checkout.session.completed for unknown session ID: {}", sessionId);
        }
    }
}
