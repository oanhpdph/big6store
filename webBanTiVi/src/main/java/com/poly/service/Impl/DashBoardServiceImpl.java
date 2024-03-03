package com.poly.service.Impl;

import com.poly.entity.Bill;
import com.poly.entity.Coupon;
import com.poly.entity.Product;
import com.poly.entity.Voucher;
import com.poly.repository.BillRepos;
import com.poly.repository.CouponRepository;
import com.poly.repository.ProductRepository;
import com.poly.repository.VoucherRepository;
import com.poly.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashBoardServiceImpl implements DashBoardService {

    @Autowired
    BillRepos billRepos;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    VoucherRepository voucherRepository;


    @Autowired
    CouponRepository couponRepository;

//    @Override
//    public List<Bill> getBillByDate(Date date) {
//        List<Bill> orders = billRepos.findBillByDate(date);
//        return  orders ;
//    }
//
//    @Override
//    public List<Bill> getBillReturned(Date date) {
//        List<Bill> orders = billRepos.findBillByDate(date);
//        List<Bill> listBillReturn = new ArrayList<>();
//        for(Bill bill : orders){
//            if(bill.getBillStatus().getStatus().equals("RR")){
//                listBillReturn.add(bill);
//            }
//        }
//        return listBillReturn;
//    }

    @Override
    public List<Bill> getAllBill() {
        return this.billRepos.findAll();
    }

    @Override
    public List<Bill> getAllBillReturn() {
        List<Bill> orders = this.billRepos.findAll();
        List<Bill> listBillReturn = new ArrayList<>();
        for(Bill bill : orders){
            if(bill.getBillStatus().getCode().equals("RR")){
                listBillReturn.add(bill);
            }
        }
        return listBillReturn;
    }
    @Override
    public List<Bill> getAllBillProcessing() {
        List<Bill> orders = this.billRepos.findAll();
        List<Bill> listBillPro = new ArrayList<>();
        for(Bill bill : orders){
            if(bill.getBillStatus().getCode().equals("WP")){
                listBillPro.add(bill);
            }
        }
        return listBillPro;
    }
    @Override
    public List<Bill> getAllBillDelivering() {
        List<Bill> orders = this.billRepos.findAll();
        List<Bill> listBillPro = new ArrayList<>();
        for(Bill bill : orders){
            if(bill.getBillStatus().getCode().equals("DE")){
                listBillPro.add(bill);
            }
        }
        return listBillPro;
    }

    @Override
    public List<Product> getProductCount() {
        return this.productRepository.findAll();
    }

    @Override
    public List<Voucher> getVoucherCount() {
       List<Voucher> listVoucher = this.voucherRepository.findAll();
       return listVoucher.stream().filter(voucher -> voucher.getActive() == true).toList();
    }

    @Override
    public List<Coupon> getDisCount() {
        List<Coupon> listCoupon = this.couponRepository.findAll();
        return listCoupon.stream().filter(voucher -> voucher.isActive() == true).toList();
    }
}
