package com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.shop.entity.Estimate;
import com.shop.entity.PlywoodStock;
import com.shop.service.EstimateService;
import com.shop.service.PlywoodStockService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/estimate")
public class EstimateController {

    @Autowired
    private EstimateService estimateService;

    @Autowired
    private PlywoodStockService plywoodStockService;

    @GetMapping("/generate")
    public String generateForm(Model model) {
        model.addAttribute("estimate", new Estimate());
        model.addAttribute("plyThicknesses", getPlyThicknesses());
        return "generate-estimate";
    }

    @PostMapping("/generate")
    public String generate(@RequestParam String customerName,
                          @RequestParam String category,
                          @RequestParam List<String> itemNames,
                          @RequestParam List<String> companies,
                          @RequestParam List<String> priceStrings,
                          @RequestParam List<String> quantityStrings,
                          @RequestParam(defaultValue = "0") double discount,
                          Model model) {
        Estimate estimate = new Estimate();
        estimate.setCustomerName(customerName);
        estimate.setCategory(category);
        estimate.setDate(LocalDateTime.now());
        estimate.setDiscount(discount);

        List<String> items = new ArrayList<>();
        List<String> selectedCompanies = new ArrayList<>();
        List<Double> prices = new ArrayList<>();
        List<Integer> qtys = new ArrayList<>();
        double total = 0;

        for (int i = 0; i < itemNames.size(); i++) {
            String itemName = itemNames.get(i);
            String company = i < companies.size() ? companies.get(i) : "N/A";
            String priceText = i < priceStrings.size() ? priceStrings.get(i) : "0";
            String qtyText = i < quantityStrings.size() ? quantityStrings.get(i) : "0";

            double price = 0;
            int quantity = 0;
            try {
                price = priceText != null && !priceText.isBlank() ? Double.parseDouble(priceText) : 0;
            } catch (NumberFormatException ignored) {
            }
            try {
                quantity = qtyText != null && !qtyText.isBlank() ? Integer.parseInt(qtyText) : 0;
            } catch (NumberFormatException ignored) {
            }

            if (itemName != null && !itemName.isBlank() && price > 0 && quantity > 0) {
                items.add(itemName);
                selectedCompanies.add(company);
                prices.add(price);
                qtys.add(quantity);
                total += price * quantity;
            }
        }

        estimate.setItems(items);
        estimate.setCompanies(selectedCompanies);
        estimate.setPrices(prices);
        estimate.setQuantities(qtys);
        estimate.setTotalAmount(total);
        estimate.setFinalAmount(total - discount);

        estimateService.save(estimate);

        model.addAttribute("estimate", estimate);
        return "estimate-view";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("estimates", estimateService.getAll());
        return "estimate-list";
    }

    private List<String> getPlyThicknesses() {
        List<String> thicknesses = new ArrayList<>();
        if (plywoodStockService != null) {
            for (PlywoodStock stock : plywoodStockService.getAll()) {
                if (stock.getSize() != null && stock.getSize().contains(" - ")) {
                    String thickness = stock.getSize().split(" - ", 2)[0].trim();
                    if (!thickness.isBlank() && !thicknesses.contains(thickness)) {
                        thicknesses.add(thickness);
                    }
                }
            }
        }
        if (thicknesses.isEmpty()) {
            thicknesses.addAll(List.of("12mm", "15mm", "19mm"));
        }
        thicknesses.sort(Comparator.naturalOrder());
        return thicknesses;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        estimateService.deleteById(id);
        return "redirect:/estimate/list";
    }
}