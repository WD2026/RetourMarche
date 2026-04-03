package com.example.shop.Payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Value("${stripe.public.key}")
    private String stripePublicKey;

    @GetMapping("/success")
    public String success(@RequestParam("session_id") String sessionId, Model model) {
        // Here we could verify the session with Stripe if needed
        model.addAttribute("sessionId", sessionId);
        return "redirect:/basket/checkout/success"; // Redirect to existing success handler
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "redirect:/basket/checkout?error=payment_cancelled";
    }
}
