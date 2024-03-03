package com.poly.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductDetailDto {
    Integer id;
    String sku = "";
    String nameProduct;
    Integer group = 0;
    boolean active;
    Integer brand;
    List<ProductDetailListDto> listProduct;
    List<Attribute> product;
    Integer page = 1;
    Integer size = 10;
    Integer sort = 1;
    String image;
    BigDecimal reduceMoney;
    BigDecimal price;
    float point;
    Integer quantityEvalute;
    List<Integer> listBrand;
    String key;
}
