package com.poly.service.Impl;

import com.poly.entity.ImageReturned;
import com.poly.repository.ImageReturnRepository;
import com.poly.service.ImageReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageReturnServiceImpl implements ImageReturnService {

    @Autowired
    ImageReturnRepository imageReturnRepository;

    @Override
    public ImageReturned save(ImageReturned image) {
        return this.imageReturnRepository.save(image);
    }


    @Override
    public void delete(Integer id) {
        this.imageReturnRepository.deleteById(id);
    }

    @Override
    public List<ImageReturned> findAll() {
        return this.imageReturnRepository.findAll();
    }

    @Override
    public ImageReturned findById(Integer id) {
        return this.imageReturnRepository.findById(id).get();
    }

    @Override
    public ImageReturned findByBillid(Integer id) {
        return null;
    }
}
