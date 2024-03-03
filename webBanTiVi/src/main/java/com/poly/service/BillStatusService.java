package com.poly.service;

import com.poly.entity.BillStatus;

import java.util.List;

public interface BillStatusService{

    List<BillStatus> getAll();

    BillStatus getOneBycode(String code);

    BillStatus add(BillStatus billStatus);

    BillStatus update(BillStatus billStatus, Integer id);

    Boolean delete(Integer id);

    BillStatus findById(Integer id);

}
