
package com.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.entity.Product;
import com.shop.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public List<Product> getAll(){
        return repo.findAll();
    }

    public List<Product> getByCategory(String category){
        return repo.findByCategory(category);
    }

    public void save(Product p){
        repo.save(p);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public Product getById(Long id){
        return repo.findById(id).orElse(null);
    }
}
