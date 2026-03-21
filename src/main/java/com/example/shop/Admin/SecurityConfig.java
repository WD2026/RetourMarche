package com.example.shop.Admin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/register", "/product/**", "/css/**", "/js/**", "/images/**", "/icones/**", "/*.png", "/h2-console/**").permitAll()
                .requestMatchers("/actuator/**").permitAll() // Secure this in production
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(request -> "GET".equals(request.getMethod()) && "/logout".equals(request.getRequestURI()))
                .logoutSuccessUrl("/")
                .permitAll()
            );

        return http.build();
    }
}
