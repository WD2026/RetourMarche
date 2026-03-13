package com.example.shop.Product;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.shop.Accessoire.Accessoire;
import com.example.shop.Accessoire.AccessoireRepository;
import com.example.shop.Accessoire.TypeAccessoire;
import com.example.shop.Smartphone.Smartphone;
import com.example.shop.Smartphone.SmartphoneRepository;
import com.example.shop.User.User;

import jakarta.servlet.http.HttpSession;

/**
 * Contrôleur pour la gestion des produits (ajout, formulaires).
 */
@Controller
public class ProductController {

    @Autowired
    private SmartphoneRepository smartphoneRepository;

    @Autowired
    private AccessoireRepository accessoireRepository;

    @GetMapping("/addSmartphone")
    public String showAddSmartphoneForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        model.addAttribute("smartphone", new Smartphone());

        Map<Integer, String> stockages = new LinkedHashMap<>();
        stockages.put(64, "64 GB");
        stockages.put(128, "128 GB");
        stockages.put(256, "256 GB");
        stockages.put(512, "512 GB");
        stockages.put(1024, "1 TB");
        stockages.put(2048, "2 TB");
        model.addAttribute("stockages", stockages);

        Map<String, String> couleurs = new LinkedHashMap<>();
        couleurs.put("Noir", "Noir");
        couleurs.put("Blanc", "Blanc");
        couleurs.put("Rouge", "Rouge");
        couleurs.put("Bleu", "Bleu");
        couleurs.put("Vert", "Vert");
        couleurs.put("Or", "Or");
        couleurs.put("Argent", "Argent");
        couleurs.put("Gris sidéral", "Gris sidéral");
        model.addAttribute("couleurs", couleurs);

        Map<String, String> conditions = new LinkedHashMap<>();
        conditions.put("Neuf", "Neuf");
        conditions.put("Très bon état", "Très bon état");
        conditions.put("Bon état", "Bon état");
        conditions.put("Mauvais état", "Mauvais état");
        model.addAttribute("conditions", conditions);

        return "addSmartphone";
    }

    public static Map<String, String> getColorMap() {
        Map<String, String> couleurs = new LinkedHashMap<>();
        couleurs.put("Noir", "Noir");
        couleurs.put("Blanc", "Blanc");
        couleurs.put("Rouge", "Rouge");
        couleurs.put("Bleu", "Bleu");
        couleurs.put("Vert", "Vert");
        couleurs.put("Or", "Or");
        couleurs.put("Argent", "Argent");
        couleurs.put("Gris sidéral", "Gris sidéral");
        return couleurs;
    }

    @PostMapping("/addSmartphone")
    public String addSmartphone(@ModelAttribute Smartphone smartphone) {
        smartphoneRepository.save(smartphone);
        return "redirect:/dashboard?section=smartphone";
    }

    // ---- AJOUT D'ACCESSOIRE ----
    @GetMapping("/addAccessoire")
    public String showAddAccessoireForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (!"ADMIN".equals(user.getRole())) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        model.addAttribute("accessoire", new Accessoire());
        model.addAttribute("types", TypeAccessoire.values());
        model.addAttribute("smartphones", smartphoneRepository.findAll());
        return "addAccessoire";
    }

    @PostMapping("/addAccessoire")
    public String addAccessoire(@ModelAttribute Accessoire accessoire) {
        accessoireRepository.save(accessoire);
        return "redirect:/dashboard?section=accessoire";
    }
}
