package com.poly.service.Impl;

import com.poly.entity.GroupProduct;
import com.poly.repository.GroupProductRepo;
import com.poly.service.GroupProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupProductImpl implements GroupProductService {

    @Autowired
    private GroupProductRepo groupProductRepo;

    @Override
    public List<GroupProduct> findAll() {
        return groupProductRepo.findAll();
    }

    @Override
    public GroupProduct findById(Integer id) {
        return groupProductRepo.findById(id).get();
    }
}

