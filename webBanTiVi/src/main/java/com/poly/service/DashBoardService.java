package com.poly.service;

import com.poly.entity.Bill;
import com.poly.entity.Coupon;
import com.poly.entity.Product;
import com.poly.entity.Voucher;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface DashBoardService {
   List<Bill> getAllBill();
   List<Bill> getAllBillReturn();
   List<Bill> getAllBillProcessing();
   List<Bill> getAllBillDelivering();
   List<Product> getProductCount();
   List<Voucher> getVoucherCount();
   List<Coupon> getDisCount();
}
