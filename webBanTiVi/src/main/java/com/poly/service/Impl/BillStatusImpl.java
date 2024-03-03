package com.poly.service.Impl;

import com.poly.entity.BillStatus;
import com.poly.repository.BillStatusRepos;
import com.poly.service.BillStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BillStatusImpl implements BillStatusService {
    @Autowired
    private BillStatusRepos billStatusRepos;

    @Override
    public List<BillStatus> getAll() {
        return billStatusRepos.findAll();
    }

    @Override
    public BillStatus getOneBycode(String code) {
        Optional<BillStatus> optionalBillStatus = billStatusRepos.findByCode(code);
        if (optionalBillStatus.isPresent()) {
            return optionalBillStatus.get();
        }
        return null;
    }

    @Override
    public BillStatus add(BillStatus billStatus) {
        return billStatusRepos.save(billStatus);
    }

    @Override
    public BillStatus update(BillStatus billStatus, Integer id) {
        Optional<BillStatus> optional = billStatusRepos.findById(id);
        if (optional.isPresent()) {
            BillStatus status = optional.get();
            status.setStatus(billStatus.getStatus());
            status.setDescription(billStatus.getDescription());
            billStatusRepos.save(status);
        }
        return null;
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<BillStatus> optional = billStatusRepos.findById(id);
        if (optional.isPresent()) {
            billStatusRepos.delete(optional.get());
            return true;
        }
        return false;
    }

    @Override
    public BillStatus findById(Integer id) {
        return this.billStatusRepos.findById(id).get();
    }
}
