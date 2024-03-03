package com.poly.service.Impl;

import com.poly.entity.Brand;
import com.poly.entity.Product;
import com.poly.repository.BrandRepo;
import com.poly.repository.ProductRepository;
import com.poly.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandImpl implements BrandService {

    @Autowired
    private BrandRepo brandRepo;

    @Autowired
    private ProductRepository productRepository;


    @Override
    public List<Brand> findAll() {
        return brandRepo.findAll();
    }

    @Override
    public Brand findById(Integer id) {
        Optional<Brand> optional = brandRepo.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public Brand update(Brand brand) {
        Optional<Brand> optional = brandRepo.findById(brand.getId());
        if (optional.isPresent()) {
            Brand brand1 = optional.get();
            if (brand.getName() != null) {
                brand1.setName(brand.getName());
            }
            brand1.setActive(brand.isActive());
            if (brand.isActive() == false) {
                List<Product> list = productRepository.findByBrand(brand1);
                for (Product product : list) {
                    product.setActive(false);
                    productRepository.save(product);
                }
            }
            return brandRepo.save(brand1);
        }
        return null;
    }

    @Override
    public Brand save(Brand brand) {
        Brand brand1 = brandRepo.findName(brand.getName());
        if (brand1 == null) {
            return brandRepo.save(brand);
        }
        return null;
    }

    @Override
    public Brand findName(String brand) {
        Brand brand1 = brandRepo.findName(brand);
        if (brand1 != null) {
            return brand1;
        }
        return null;
    }
}
