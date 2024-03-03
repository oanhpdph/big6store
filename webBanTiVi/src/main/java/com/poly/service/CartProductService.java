package com.poly.service;

import com.poly.dto.CartDto;
import com.poly.entity.CartProduct;
import com.poly.entity.idClass.CartProductId;

import java.util.List;
import java.util.Optional;

public interface CartProductService {

    List<CartProduct> getAll();

    CartProduct save(CartDto cp);

    CartProduct update(CartProduct cp);

    Boolean delete(CartProductId id);

    CartProduct edit(CartProductId id);

    Optional<CartProduct> getOne(CartProductId id);

}

