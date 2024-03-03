package com.poly.service;

import com.poly.entity.Field;
import com.poly.entity.ProductDetailField;

import java.util.List;

public interface ProductDetailFieldService {

    ProductDetailField save(ProductDetailField productDetailField);

    List<ProductDetailField> findByField(Field field);

}
