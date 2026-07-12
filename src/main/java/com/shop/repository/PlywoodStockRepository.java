package com.shop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shop.entity.PlywoodStock;

public interface PlywoodStockRepository extends JpaRepository<PlywoodStock, Long> {
    List<PlywoodStock> findByCompanyName(String companyName);
    PlywoodStock findByCompanyNameAndSize(String companyName, String size);

    @Query("select p from PlywoodStock p where p.companyName = :companyName and (p.stockType = :stockType or (p.stockType is null and :stockType = 'Plywood'))")
    List<PlywoodStock> findByStockTypeAndCompanyName(@Param("stockType") String stockType, @Param("companyName") String companyName);

    @Query("select p from PlywoodStock p where p.companyName = :companyName and p.size = :size and (p.stockType = :stockType or (p.stockType is null and :stockType = 'Plywood')) order by p.id asc")
    List<PlywoodStock> findByStockTypeAndCompanyNameAndSize(@Param("stockType") String stockType, @Param("companyName") String companyName, @Param("size") String size);
}
