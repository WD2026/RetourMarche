package com.example.shop.Common;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CircuitBreakerController {

    @GetMapping("/test-circuit-breaker")
    @CircuitBreaker(name = "mainService", fallbackMethod = "fallback")
    public String testCircuitBreaker() {
        // Simuler un appel qui pourrait échouer
        if (Math.random() > 0.5) {
            throw new RuntimeException("Service failure!");
        }
        return "Service is working fine!";
    }

    public String fallback(Exception e) {
        return "Fallback response: The service is currently unavailable. Please try again later.";
    }
}
