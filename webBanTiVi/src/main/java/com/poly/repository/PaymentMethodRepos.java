package com.poly.repository;

import com.poly.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepos extends JpaRepository<PaymentMethod,Integer> {
}
