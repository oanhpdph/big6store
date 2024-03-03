package com.poly.repository;

import com.poly.entity.Cart;
import com.poly.entity.CartProduct;
import com.poly.entity.Product;
import com.poly.entity.idClass.CartProductId;
import com.poly.service.CartProductService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.UUID;

@Repository
public interface CartProductRepos extends JpaRepository<CartProduct, CartProductId> {
//    @Query("delete from cart_product where cart_id = ?1 and product_id = ?2")
//    void deleteById(Integer id, Integer id1);
//
//    Map<Object, Object> findById(Integer id);
}
