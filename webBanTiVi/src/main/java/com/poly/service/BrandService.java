package com.poly.service;

import com.poly.entity.Brand;

import java.util.List;

public interface BrandService {
    List<Brand> findAll();

    Brand findById(Integer id);

    Brand update(Brand brand);

    Brand save(Brand brand);

    Brand findName(String brand);
}
