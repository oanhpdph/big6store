package com.poly.service;

import com.poly.entity.BillProduct;

import java.util.List;

public interface BillProductService {

    List<BillProduct> getAll();

    void save(BillProduct bp);

    void delete(Integer id);

    BillProduct edit(Integer id);


   List<BillProduct> findBillByStatus(Integer status);
}
