package com.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.shop.entity.Estimate;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {}