package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shop.entity.Bill;

public interface BillRepository extends JpaRepository<Bill, Long> {}