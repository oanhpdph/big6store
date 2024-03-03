package com.poly.service;

import com.poly.entity.GroupProduct;

import java.util.List;

public interface GroupProductService {
    List<GroupProduct> findAll();

    GroupProduct findById(Integer id);
}
