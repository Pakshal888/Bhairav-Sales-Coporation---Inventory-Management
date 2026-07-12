package com.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.entity.Bill;
import com.shop.repository.BillRepository;

@Service
public class BillService {

    @Autowired
    private BillRepository repo;

    public List<Bill> getAll(){
        return repo.findAll();
    }

    public void save(Bill b){
        repo.save(b);
    }

    public void deleteById(Long id) {
        repo.deleteById(id);
    }

    public Bill getById(Long id){
        return repo.findById(id).orElse(null);
    }
}