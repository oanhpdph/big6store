package com.poly.entity.idClass;

import com.poly.entity.Coupon;
import com.poly.entity.ProductDetail;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponProductId implements Serializable {
    private Coupon coupon;
    private ProductDetail productDetail;

}
