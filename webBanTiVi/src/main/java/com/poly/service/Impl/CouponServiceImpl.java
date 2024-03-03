package com.poly.service.Impl;


import com.poly.entity.Coupon;
import com.poly.entity.ProductDetail;
import com.poly.repository.CouponRepository;
import com.poly.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    CouponRepository couponRepository;

    public List<ProductDetail> getAllByIdAndKeyword(Integer id, String keyword){
    return couponRepository.getAllByIdAndKeyword(id,keyword);
    }
//    public List<CouponRes> getAllCouponRes(Date date){
//        return couponRepository.getCouponRes(date);
//    }
    public List<Coupon> getAllByActive(Date date){
        return couponRepository.getAllByActive(date);
    }

    @Override
    public Coupon save(Coupon coupon) {
        return this.couponRepository.save(coupon);
    }

    @Override
    public void deleteById(Integer id) {
        this.couponRepository.deleteById(id);
    }

    @Override
    public List<Coupon> findAll() {
        return this.couponRepository.findAll();
    }

    @Override
    public Optional<Coupon> findById(Integer id) {
        return this.couponRepository.findById(id);
    }
}