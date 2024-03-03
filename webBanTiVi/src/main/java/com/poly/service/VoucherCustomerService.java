package com.poly.service;

import com.poly.dto.SearchVoucherDto;
import com.poly.dto.VoucherCustomerRes;
import com.poly.entity.VoucherCustomer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface VoucherCustomerService {

    VoucherCustomer save(VoucherCustomerRes voucher);

    void delete(Integer id);

    List<VoucherCustomer> findByUser(Integer user);

    Optional<VoucherCustomer> findById(Integer id);

    Page<VoucherCustomer> loadData(SearchVoucherDto searchVoucherDto, Pageable pageable);

    void updateById(VoucherCustomerRes voucher, int id);
}