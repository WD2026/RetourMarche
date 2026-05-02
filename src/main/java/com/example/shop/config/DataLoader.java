package com.example.shop.config;

import com.example.shop.Accessoire.Accessoire;
import com.example.shop.Accessoire.AccessoireRepository;
import com.example.shop.Accessoire.TypeAccessoire;
import com.example.shop.Smartphone.Smartphone;
import com.example.shop.Smartphone.SmartphoneRepository;
import com.example.shop.User.User;
import com.example.shop.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SmartphoneRepository smartphoneRepository;
    private final AccessoireRepository accessoireRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadAdminUser();
        loadDemoProducts();
    }

    private void loadAdminUser() {
        if (!userRepository.existsByEmail("admin@gmail.com")) {
            User admin = new User();
            admin.setNom("Admin");
            admin.setPrenom("System");
            admin.setEmail("admin@gmail.com");
            
            String initialAdminPassword = System.getenv("ADMIN_PASSWORD");
            if (initialAdminPassword == null || initialAdminPassword.isBlank()) {
                log.error("ADMIN_PASSWORD environment variable is not set. Skipping admin user creation.");
                return;
            }
            admin.setPassword(passwordEncoder.encode(initialAdminPassword));
            admin.setTelephone("0123456789"); // Valid French format
            admin.setRole("ADMIN");
            userRepository.save(admin);
            log.info("Admin user created successfully.");
        } else {
            log.info("Admin user already exists.");
        }
    }

    private void loadDemoProducts() {
        if (smartphoneRepository.count() == 0 && accessoireRepository.count() == 0) {
            log.info("Loading demo products...");
            
            Smartphone iphone = new Smartphone();
            iphone.setName("iPhone 14 Pro");
            iphone.setBrand("Apple");
            iphone.setColor("Space Black");
            iphone.setStorageCapacity(256);
            iphone.setCondition("Neuf");
            iphone.setPrice(1299.99);
            iphone.setNewPrice(1299.99);
            iphone.setStock(15);
            iphone.setDescription("The latest iPhone with Dynamic Island.");
            iphone.setImageUrl("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/iphone-14-pro-model-unselect-gallery-2-202209_GEO_EMEA?wid=5120&hei=2880&fmt=p-jpg&qlt=80&.v=1660753617539");
            smartphoneRepository.save(iphone);

            Smartphone galaxy = new Smartphone();
            galaxy.setName("Galaxy S23 Ultra");
            galaxy.setBrand("Samsung");
            galaxy.setColor("Phantom Black");
            galaxy.setStorageCapacity(512);
            galaxy.setCondition("Reconditionné - Excellent");
            galaxy.setPrice(999.00);
            galaxy.setNewPrice(1399.00);
            galaxy.setStock(8);
            galaxy.setDescription("Premium Android smartphone with S Pen.");
            galaxy.setImageUrl("https://images.samsung.com/is/image/samsung/p6pim/fr/sm-s918bzkgeub/gallery/fr-galaxy-s23-s918-sm-s918bzkgeub-534886905?$650_519_PNG$");
            smartphoneRepository.save(galaxy);

            Accessoire charger = new Accessoire();
            charger.setName("20W USB-C Power Adapter");
            charger.setType(TypeAccessoire.CHARGEUR);
            charger.setPrice(25.00);
            charger.setStock(50);
            charger.setDescription("Fast charger compatible with iPhone and Galaxy.");
            charger.setImageUrl("https://store.storeimages.cdn-apple.com/4668/as-images.apple.com/is/MHJE3?wid=1144&hei=1144&fmt=jpeg&qlt=90&.v=1592509172000");
            charger.setSmartphones(List.of(iphone, galaxy));
            accessoireRepository.save(charger);

            Accessoire case_s23 = new Accessoire();
            case_s23.setName("Galaxy S23 Ultra Leather Case");
            case_s23.setType(TypeAccessoire.COQUE);
            case_s23.setPrice(49.99);
            case_s23.setStock(20);
            case_s23.setDescription("Premium leather case for Galaxy S23 Ultra.");
            case_s23.setImageUrl("https://images.samsung.com/is/image/samsung/p6pim/fr/ef-vs918lbegww/gallery/fr-leather-case-for-galaxy-s23-ultra-ef-vs918-ef-vs918lbegww-534887349?$650_519_PNG$");
            case_s23.setSmartphones(List.of(galaxy));
            accessoireRepository.save(case_s23);
            
            log.info("Demo products loaded successfully.");
        } else {
            log.info("Products already exist in the database. Skipping demo data injection.");
        }
    }
}
