package com.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.entity.PlywoodStock;
import com.shop.repository.PlywoodStockRepository;

@Service
public class PlywoodStockService {

    @Autowired
    private PlywoodStockRepository repo;

    public List<PlywoodStock> getAll() {
        return repo.findAll();
    }

    public List<PlywoodStock> getByCompanyName(String companyName) {
        return repo.findByCompanyName(companyName);
    }

    public List<PlywoodStock> getByStockTypeAndCompanyName(String stockType, String companyName) {
        return repo.findByStockTypeAndCompanyName(stockType, companyName);
    }

    public PlywoodStock getByStockTypeAndCompanyNameAndSize(String stockType, String companyName, String size) {
        List<PlywoodStock> results = repo.findByStockTypeAndCompanyNameAndSize(stockType, companyName, size);
        return results.isEmpty() ? null : results.get(0);
    }

    public PlywoodStock getById(Long id) {
        return repo.findById(id).orElse(null);
    }

    public void save(PlywoodStock stock) {
        repo.save(stock);
    }

    public void saveAll(List<PlywoodStock> stocks) {
        repo.saveAll(stocks);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }
}
