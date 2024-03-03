package com.poly.repository;

import com.poly.entity.Coupon;
import com.poly.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer> {
//    @Query(value = "select p.id, p.value, p.image, p.active, cp.date_Start, cp.date_End,cp.id_product " +
//            "from Discount p join Discount_Product cp on p.id = cp.id_discount where cp.date_End>=?1 and p.active=1", nativeQuery = true)
//    List<CouponRes> getCouponRes(Date date);
    @Query("select c from Coupon c where c.dateEnd>=?1 and c.active=true")
    List<Coupon> getAllByActive(Date date);
    @Query("select p from Coupon c join ProductDetail p on c.id = p.coupon.id " +
            "where c.id=?1 and p.sku like ?2")
    List<ProductDetail> getAllByIdAndKeyword(Integer id, String keyword);
}
