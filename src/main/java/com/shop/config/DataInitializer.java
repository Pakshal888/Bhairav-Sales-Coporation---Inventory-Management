package com.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.shop.entity.Product;
import com.shop.entity.User;
import com.shop.service.ProductService;
import com.shop.service.UserService;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Create admin user
        if (userService.findByUsername("admin") == null) {
            User admin = new User("admin", "admin123", "ADMIN");
            userService.save(admin);
        }

        // Create normal user
        if (userService.findByUsername("user") == null) {
            User user = new User("user", "user123", "USER");
            userService.save(user);
        }

        // Add sample products if none exist
        if (productService.getAll().isEmpty()) {
            Product p1 = new Product();
            p1.setBrand("ABC Hardware");
            p1.setSize("10mm");
            p1.setPurchaseRate(100.0);
            p1.setStockRate(120.0);
            p1.setThickness(1);
            p1.setQuantity(50);
            p1.setCategory("Hardware");
            productService.save(p1);

            Product p2 = new Product();
            p2.setBrand("XYZ Electric");
            p2.setSize("5A");
            p2.setPurchaseRate(200.0);
            p2.setStockRate(250.0);
            p2.setThickness(1);
            p2.setQuantity(30);
            p2.setCategory("Electric");
            productService.save(p2);

            Product p3 = new Product();
            p3.setBrand("Pipe Co");
            p3.setSize("1inch");
            p3.setPurchaseRate(150.0);
            p3.setStockRate(180.0);
            p3.setThickness(1);
            p3.setQuantity(40);
            p3.setCategory("Plumbing");
            productService.save(p3);

            Product p4 = new Product();
            p4.setBrand("Fancy Items");
            p4.setSize("Decor");
            p4.setPurchaseRate(300.0);
            p4.setStockRate(350.0);
            p4.setThickness(1);
            p4.setQuantity(20);
            p4.setCategory("Fancyware");
            productService.save(p4);

            Product p5 = new Product();
            p5.setBrand("Sanitary Plus");
            p5.setSize("Basin");
            p5.setPurchaseRate(400.0);
            p5.setStockRate(450.0);
            p5.setThickness(1);
            p5.setQuantity(15);
            p5.setCategory("Sanitaryware");
            productService.save(p5);
        }
    }
}