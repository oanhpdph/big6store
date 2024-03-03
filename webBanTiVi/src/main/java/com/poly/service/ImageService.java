package com.poly.service;

import com.poly.entity.Image;

public interface ImageService {
    Image add(Image image);

    Image findById(Integer id);
}
