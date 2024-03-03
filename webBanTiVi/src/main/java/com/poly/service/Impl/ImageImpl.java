package com.poly.service.Impl;

import com.poly.entity.Image;
import com.poly.repository.ImageRepo;
import com.poly.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ImageImpl implements ImageService {

    @Autowired
    private ImageRepo imageRepo;

    @Override
    public Image add(Image image) {
        return imageRepo.save(image);
    }

    @Override
    public Image findById(Integer id) {
        Optional<Image> optional = imageRepo.findById(id);
        if (optional.isPresent()) {
            return imageRepo.findById(id).get();
        }
        return null;
    }
}
