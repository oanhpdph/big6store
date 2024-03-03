package com.poly.service;

import com.poly.dto.ProductDetailDto;
import com.poly.entity.Product;
import com.poly.entity.ProductDetail;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;


public interface ProductService {

    Product save(Product product);

    void delete(Integer id);

    Page<Product> findAll(ProductDetailDto productDetailDto);

    List<Product> findAll();

    Product findById(Integer id);

    Product update(Integer id,ProductDetailDto product);

    List<ProductDetailDto> getProduct(List<ProductDetailDto> listReturn, List<Product> listInput);

    BigDecimal getReduceMoney(ProductDetail product);

//    List<Product> findSameProduct(String same);

}
