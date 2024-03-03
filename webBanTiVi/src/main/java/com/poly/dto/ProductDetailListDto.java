package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailListDto {
    Integer id;
    List<ImageDto> image;
    List<Attribute> listAttributes;
    BigDecimal priceImport;
    BigDecimal priceExport;
    Integer quantity;
    String sku = "";
    boolean active;
    Integer page = 1;
    Integer size = 10;
    Integer sort = 1;
    String key;
}
