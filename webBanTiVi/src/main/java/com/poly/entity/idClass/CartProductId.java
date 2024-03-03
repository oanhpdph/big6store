package com.poly.entity.idClass;

import com.poly.entity.Cart;
import com.poly.entity.ProductDetail;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@EqualsAndHashCode
@Setter
@Getter
public class CartProductId implements Serializable {
    private ProductDetail product;
    private Cart cart;
}
