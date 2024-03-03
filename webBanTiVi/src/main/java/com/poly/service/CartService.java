package com.poly.service;

import com.poly.entity.Cart;
import com.poly.entity.CartProduct;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {

    List<CartProduct> getitems();

    List<Cart> getAll();

    Cart save(Cart cart);

    List<CartProduct> add(Integer id, Integer qty);

    List<CartProduct> update(int id, Integer qty);

    List<CartProduct> delete(Integer id);

    void clear();

    BigDecimal getTotalNoLogin(List<CartProduct> list);

    int getTotal();

    BigDecimal getAmount();

    Cart getOneByUser(Integer id);

    List<BigDecimal> getListReduceMoney(List<CartProduct> list);
}
