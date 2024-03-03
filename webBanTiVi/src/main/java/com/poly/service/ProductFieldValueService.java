package com.poly.service;

import com.poly.entity.Product;
import com.poly.entity.ProductFieldValue;

import java.util.List;

public interface ProductFieldValueService {
    ProductFieldValue save(ProductFieldValue productFieldValue);
    List<ProductFieldValue> findByProduct(Product product);

}
