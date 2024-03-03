package com.poly.service;

import com.poly.entity.ImageReturned;

import java.util.List;

public interface ImageReturnService {
    ImageReturned save(ImageReturned image);

    void delete(Integer id);

    List<ImageReturned> findAll();

    ImageReturned findById(Integer id);

    ImageReturned findByBillid( Integer id);
}
