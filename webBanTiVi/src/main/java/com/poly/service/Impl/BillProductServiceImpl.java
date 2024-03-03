package com.poly.service.Impl;

import com.poly.entity.BillProduct;
import com.poly.repository.BillProductRepos;
import com.poly.service.BillProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillProductServiceImpl implements BillProductService {
    @Autowired
    private BillProductRepos repos;

    @Override
    public List<BillProduct> getAll() {
        return repos.findAll();
    }

    @Override
    public void save(BillProduct bp) {
        repos.save(bp);
    }

    @Override
    public void delete(Integer id) {
        repos.deleteById(id);
    }

    @Override
    public BillProduct edit(Integer id) {
        return repos.findById(id).get();
    }

    @Override
    public List<BillProduct> findBillByStatus(Integer status) {
        return this.repos.findBillByStatus(status);
    }
}
