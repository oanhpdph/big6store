package com.poly.service;

import com.poly.dto.ProductDetailDto;
import com.poly.dto.ProductDetailListDto;
import com.poly.entity.Product;
import com.poly.entity.ProductDetail;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductDetailService {
    Product saveList(ProductDetailDto dto);

    ProductDetail findById(Integer id);

    Page<ProductDetail> findAll(ProductDetailListDto productDetailListDto);

    List<ProductDetail> findAll();

    Boolean delete(Integer id);

    ProductDetail update(ProductDetailListDto productDetailListDto);

    ProductDetail save(ProductDetailDto productDetailDto);


}
