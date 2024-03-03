package com.poly.service;

import com.poly.entity.Coupon;

import java.util.List;
import java.util.Optional;

public interface CouponService {
    Coupon save(Coupon coupon);

    void deleteById(Integer id);

    List<Coupon> findAll();

    Optional<Coupon> findById(Integer id);
}
