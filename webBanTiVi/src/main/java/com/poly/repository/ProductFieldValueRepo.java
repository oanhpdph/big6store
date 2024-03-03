package com.poly.repository;

import com.poly.entity.Product;
import com.poly.entity.ProductFieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFieldValueRepo extends JpaRepository<ProductFieldValue, Integer> {

    @Query(value = "select p from ProductFieldValue p where p.product = ?1")
    List<ProductFieldValue> findByProduct(Product product);
}
