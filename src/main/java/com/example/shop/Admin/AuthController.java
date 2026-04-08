package com.example.shop.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.User.User;
import com.example.shop.User.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contrôleur gérant l'authentification (connexion, inscription).
 */

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    //intéragir avec la BDD
    @Autowired
    private UserRepository userRepository;

    //service pour encoder le mot de passe
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public String register(@jakarta.validation.Valid User user,
            org.springframework.validation.BindingResult result,
            @RequestParam String passwordConfirm,
            Model model) {

        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().get(0).getDefaultMessage();
            logger.warn("Registration failed: Validation error for {}. Message: {}", user.getEmail(), errorMessage);
            model.addAttribute("error", errorMessage);
            model.addAttribute("showRegister", true);
            model.addAttribute("user", user);
            return "login";
        }
        
        //verifier que les éléments Password et la confirmation correspondent
        if (!user.getPassword().equals(passwordConfirm)) {
            logger.warn("Registration failed: Passwords do not match for {}", user.getEmail());
            model.addAttribute("error", "Les mots de passe ne correspondent pas");
            model.addAttribute("showRegister", true);
            model.addAttribute("user", user);
            return "login";
        }

        //vérifier que le compte n'existe pas déjà avec ce mail
        if (userRepository.existsByEmail(user.getEmail())) {
            logger.warn("Registration failed: Email {} is already in use", user.getEmail());
            model.addAttribute("error", "Cet email est déjà utilisé, connectez-vous!");
            model.addAttribute("showRegister", true);
            return "login";
        }

        //vérifier que le compte n'existe pas déjà avec ce phone
        if (userRepository.existsByTelephone(user.getTelephone())) {
            logger.warn("Registration failed: Phone number {} is already in use", user.getTelephone());
            model.addAttribute("error", "Ce téléphone est déjà utilisé, connectez-vous!");
            model.addAttribute("showRegister", true);
            return "login";
        }

        //hacher le mot de passe avant de le mettre dans la BDD
        String originalPassword = user.getPassword();
        String hashed = passwordEncoder.encode(originalPassword);
        user.setPassword(hashed);
        user.setRole("USER");
        userRepository.save(user);

        logger.info("Registration successful: User {} registered successfully", user.getEmail());
        return "redirect:/login?registered=true";
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, @RequestParam(required = false) String registered, @RequestParam(required = false) boolean showRegister, Model model) {
        model.addAttribute("showRegister", showRegister);
        if (error != null) {
            model.addAttribute("error", "Email ou mot de passe incorrect.");
        }
        if (logout != null) {
            model.addAttribute("message", "Vous avez été déconnecté avec succès.");
        }
        if (registered != null) {
            model.addAttribute("message", "Inscription réussie ! Vous pouvez vous connecter.");
        }
        return "login";
    }

}
