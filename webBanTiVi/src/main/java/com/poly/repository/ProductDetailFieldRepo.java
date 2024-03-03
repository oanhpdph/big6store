package com.poly.repository;


import com.poly.entity.Field;
import com.poly.entity.ProductDetailField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailFieldRepo extends JpaRepository<ProductDetailField, Integer> {

    @Query(value = "select f from ProductDetailField f where f.field=?1")
    List<ProductDetailField> findByField(Field field);
}
