package com.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.entity.Estimate;
import com.shop.repository.EstimateRepository;

@Service
public class EstimateService {

    @Autowired
    private EstimateRepository repo;

    public List<Estimate> getAll(){
        return repo.findAll();
    }

    public void save(Estimate e){
        repo.save(e);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public Estimate getById(Long id){
        return repo.findById(id).orElse(null);
    }
}