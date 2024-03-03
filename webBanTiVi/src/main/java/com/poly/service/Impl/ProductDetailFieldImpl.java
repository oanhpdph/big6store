package com.poly.service.Impl;

import com.poly.entity.Field;
import com.poly.entity.ProductDetailField;
import com.poly.repository.ProductDetailFieldRepo;
import com.poly.service.ProductDetailFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailFieldImpl implements ProductDetailFieldService {

    @Autowired
    private ProductDetailFieldRepo productDetailFieldRepo;

    @Override
    public ProductDetailField save(ProductDetailField productDetailField) {
        return productDetailFieldRepo.save(productDetailField);
    }

    @Override
    public List<ProductDetailField> findByField(Field field) {

        return productDetailFieldRepo.findByField(field);
    }
}
