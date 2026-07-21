
package com.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.shop.entity.Product;
import com.shop.entity.PlywoodStock;
import com.shop.service.ProductService;
import com.shop.service.PlywoodStockService;

@Controller
public class ProductController {

    @Autowired
    private ProductService service;

    @Autowired
    private PlywoodStockService plywoodStockService;

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("role", role);
        model.addAttribute("products", service.getAll());
        return "dashboard";
    }

    @GetMapping("/category/{category}")
    public String category(@PathVariable String category, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("role", role);
        model.addAttribute("products", service.getByCategory(category));
        model.addAttribute("category", category);

        if ("Hardware".equalsIgnoreCase(category)) {
            model.addAttribute("stockManagerEnabled", true);
            model.addAttribute("stockTypes", List.of("Plywood", "Handles", "Laminates", "Accessories"));
            model.addAttribute("stockCompanies", List.of(
                    "Bluebird/Timberland Club Prime",
                    "Galaxy Royal",
                    "TataGold",
                    "Modi",
                    "FlushDoor(Waterproof)",
                    "FlushDoor(Local)",
                    "Duster/Timberland Max",
                    "Centring RedPly"));
            model.addAttribute("stockItems", plywoodStockService.getAll());
        } else {
            model.addAttribute("stockManagerEnabled", false);
            model.addAttribute("stockTypes", List.of());
            model.addAttribute("stockCompanies", List.of());
            model.addAttribute("stockItems", List.of());
        }

        return "category";
    }

    @PostMapping("/hardware/stock/update")
    public String updateStock(
            @RequestParam String stockType,
            @RequestParam List<String> companyNames,
            @RequestParam List<String> sizes,
            @RequestParam List<Integer> stocks,
            @RequestParam(required = false) List<String> stockIds) {
        for (int i = 0; i < companyNames.size(); i++) {
            saveOrUpdateStockRow(stockType, companyNames.get(i), sizes.get(i), stocks.get(i), stockIds != null && stockIds.size() > i ? stockIds.get(i) : null);
        }
        return "redirect:/category/Hardware";
    }

    @PostMapping(value = "/hardware/stock/update/json", produces = "application/json")
    @ResponseBody
    public List<PlywoodStock> updateStockAjax(
            @RequestParam String stockType,
            @RequestParam List<String> companyNames,
            @RequestParam List<String> sizes,
            @RequestParam List<Integer> stocks,
            @RequestParam(required = false) List<String> stockIds) {
        String company = null;
        for (int i = 0; i < companyNames.size(); i++) {
            company = companyNames.get(i);
            saveOrUpdateStockRow(stockType, companyNames.get(i), sizes.get(i), stocks.get(i), stockIds != null && stockIds.size() > i ? stockIds.get(i) : null);
        }
        return company != null ? plywoodStockService.getByStockTypeAndCompanyName(stockType, company) : List.of();
    }

    @PostMapping(value = "/hardware/stock/update/row", produces = "application/json")
    @ResponseBody
    public PlywoodStock updateStockRow(
            @RequestParam String stockType,
            @RequestParam String companyName,
            @RequestParam String size,
            @RequestParam int stock,
            @RequestParam(required = false) String stockId) {
        return saveOrUpdateStockRow(stockType, companyName, size, stock, stockId);
    }

    @GetMapping(value = "/hardware/stock/view", produces = "application/json")
    @ResponseBody
    public List<PlywoodStock> viewStock(
            @RequestParam String stockType,
            @RequestParam String companyName) {
        return plywoodStockService.getByStockTypeAndCompanyName(stockType, companyName);
    }

    private PlywoodStock saveOrUpdateStockRow(String stockType, String companyName, String size, int stock, String stockId) {
        if (stockType == null || stockType.isBlank() || companyName == null || companyName.isBlank() || size == null || size.isBlank()) {
            return null;
        }

        PlywoodStock existing = null;
        if (stockId != null && !stockId.isBlank()) {
            try {
                Long id = Long.parseLong(stockId);
                existing = plywoodStockService.getById(id);
            } catch (NumberFormatException ignored) {
            }
        }

        if (existing == null) {
            existing = plywoodStockService.getByStockTypeAndCompanyNameAndSize(stockType, companyName, size);
        }

        if (existing == null) {
            existing = new PlywoodStock();
            existing.setStockType(stockType);
            existing.setCompanyName(companyName);
            existing.setSize(size);
        }

        existing.setStock(stock);
        plywoodStockService.save(existing);
        return existing;
    }

    @PostMapping("/hardware/plywood/delete/{id}")
    public String deletePlywoodStock(@PathVariable Long id) {
        plywoodStockService.deleteById(id);
        return "redirect:/category/Hardware";
    }

    @GetMapping("/add")
    public String addForm(Model model){
        model.addAttribute("product", new Product());
        return "add-product";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Product product){
        service.save(product);
        return "redirect:/dashboard";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id, @RequestParam(required = false) String redirect) {
        service.deleteById(id);
        return "redirect:" + (redirect != null && !redirect.isBlank() ? redirect : "/dashboard");
    }
}
