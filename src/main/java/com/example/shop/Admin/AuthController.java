package com.example.shop.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.shop.User.User;
import com.example.shop.User.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Contrôleur gérant l'authentification (connexion, inscription).
 */

@Controller
public class AuthController {

    //intéragir avec la BDD
    @Autowired
    private UserRepository userRepository;

    //service pour encoder le mot de passe
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

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

        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {
        
        //recupérer le compte à l'aide du mail entré par la personne que l'on va chercher dans la BDD
        User user = userRepository.findByEmail(email);
        
        //vérifier que le compte existe avec cet e-mail et que l'on n'a pas rien récupéré
        if (user == null) {
            model.addAttribute("error", "Aucun compte n'existe avec cet email.");
            return "login";
        }

        //vérifier que le mot de passe haché de la BDD correspond à celui entré
        if (!passwordEncoder.matches(password, user.getPassword())) {
            model.addAttribute("error", "Mot de passe incorrect.");
            return "login";
        }

        session.setAttribute("user", user);
        return "redirect:/";
    }

}
