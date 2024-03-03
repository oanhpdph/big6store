package com.poly.service;

import com.poly.entity.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {
    List<PaymentMethod> getAll();

    PaymentMethod add(PaymentMethod paymentMethod);

    PaymentMethod update(PaymentMethod paymentMethod, Integer id);

    Boolean delete(Integer id);

    PaymentMethod getOne(Integer id);
}
