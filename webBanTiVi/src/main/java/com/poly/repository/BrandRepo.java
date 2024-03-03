package com.poly.repository;

import com.poly.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BrandRepo extends JpaRepository<Brand, Integer> {
    @Query(value = "select b from Brand b where b.name=?1")
    Brand findName(String name);
}
