package com.poly.repository;

import com.poly.entity.Coupon;
import com.poly.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDetailRepo extends JpaRepository<ProductDetail, Integer> {
    @Query("select p from ProductDetail p where p.sku like ?1")
    List<ProductDetail> findbySku(String keyword);

    @Transactional
    @Modifying
    @Query("update ProductDetail p set p.coupon=?1 where p.id=?2 ")
    public void addProductDiscount(Coupon coupon, Integer id);

    @Transactional
    @Modifying
    @Query("update ProductDetail p set p.coupon=null where p.id=?1 ")
    public void deleteProductDiscount(Integer id);

}
