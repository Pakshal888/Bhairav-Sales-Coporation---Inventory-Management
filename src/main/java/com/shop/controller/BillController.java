package com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.shop.entity.Bill;
import com.shop.service.BillService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @GetMapping("/generate")
    public String generateForm(Model model) {
        model.addAttribute("bill", new Bill());
        return "generate-bill";
    }

    @PostMapping("/generate")
    public String generate(@RequestParam String customerName,
                          @RequestParam List<String> itemNames,
                          @RequestParam List<String> companies,
                          @RequestParam List<String> priceStrings,
                          @RequestParam List<String> quantityStrings,
                          @RequestParam(defaultValue = "0") double discount,
                          Model model) {
        Bill bill = new Bill();
        bill.setCustomerName(customerName);
        bill.setDate(LocalDateTime.now());
        bill.setDiscount(discount);

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

        bill.setItems(items);
        bill.setCompanies(selectedCompanies);
        bill.setPrices(prices);
        bill.setQuantities(qtys);
        bill.setTotalAmount(total);
        bill.setFinalAmount(total - discount);

        billService.save(bill);

        model.addAttribute("bill", bill);
        return "bill-view";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("bills", billService.getAll());
        return "bill-list";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        billService.deleteById(id);
        return "redirect:/bill/list";
    }
}