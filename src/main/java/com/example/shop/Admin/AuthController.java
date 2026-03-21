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
    public String register(@RequestParam String nom,
            @RequestParam String prenom,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String passwordConfirm,
            @RequestParam String telephone,
            Model model) {
        
        //verifier que les éléments Password et la confirmation correspondent
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("error", "Les mots de passe ne correspondent pas");
            model.addAttribute("showRegister", true);
            return "login";
        }

        //vérifier que le compte n'existe pas déjà avec ce mail
        if (userRepository.existsByEmail(email)) {
            model.addAttribute("error", "Cet email est déjà utilisé, connectez-vous!");
            return "login";
        }

        //vérifier que le compte n'existe pas déjà avec ce phone
        if (userRepository.existsByTelephone(telephone)) {
            model.addAttribute("error", "Ce téléphone est déjà utilisé, connectez-vous!");
            return "login";
        }

        User user = new User();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setEmail(email);
        //hacher le mot de passe avant de le mettre dans la BDD
        String hashed = passwordEncoder.encode(password);
        user.setPassword(hashed);
        user.setTelephone(telephone);
        user.setRole("USER");
        userRepository.save(user);

        logger.info("New user registered: {}", email);
        return "redirect:/login?registered=true";
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, @RequestParam(required = false) String registered, Model model) {
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
