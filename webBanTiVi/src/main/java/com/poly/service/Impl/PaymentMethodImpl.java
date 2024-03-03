package com.poly.service.Impl;

import com.poly.entity.PaymentMethod;
import com.poly.repository.PaymentMethodRepos;
import com.poly.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodImpl implements PaymentMethodService {
    @Autowired
    private PaymentMethodRepos paymentMethodRepos;

    @Override
    public List<PaymentMethod> getAll() {

        return paymentMethodRepos.findAll();
    }

    @Override
    public PaymentMethod add(PaymentMethod paymentMethod) {
        return paymentMethodRepos.save(paymentMethod);
    }

    @Override
    public PaymentMethod update(PaymentMethod paymentMethod, Integer id) {
        Optional<PaymentMethod> optional= paymentMethodRepos.findById(id);
        if (optional.isPresent()) {
            PaymentMethod paymentMethod1 = optional.get();
            paymentMethod1.setActive(paymentMethod.getActive());
            paymentMethod1.setDescription(paymentMethod.getDescription());
            paymentMethod1.setPaymentMethod(paymentMethod.getPaymentMethod());
            return paymentMethodRepos.save(paymentMethod1);
        }
        return null;
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<PaymentMethod> optional= paymentMethodRepos.findById(id);
        if (optional.isPresent()) {
            paymentMethodRepos.delete(optional.get());
            return true;
        }
        return false;
    }

    @Override
    public PaymentMethod getOne(Integer id) {
        Optional<PaymentMethod> optional= paymentMethodRepos.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
}
