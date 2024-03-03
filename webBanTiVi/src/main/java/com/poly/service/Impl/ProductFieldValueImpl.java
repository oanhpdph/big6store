package com.poly.service.Impl;

import com.poly.entity.Product;
import com.poly.entity.ProductFieldValue;
import com.poly.repository.ProductFieldValueRepo;
import com.poly.service.ProductFieldValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductFieldValueImpl implements ProductFieldValueService {

    @Autowired
    private ProductFieldValueRepo productFieldValueRepo;

    @Override
    public ProductFieldValue save(ProductFieldValue productFieldValue) {
        return productFieldValueRepo.save(productFieldValue);
    }

    @Override
    public List<ProductFieldValue> findByProduct(Product product) {
        return productFieldValueRepo.findByProduct(product);
    }
}
