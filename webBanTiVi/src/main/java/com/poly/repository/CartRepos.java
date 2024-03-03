package com.poly.repository;

import com.poly.entity.Cart;
import com.poly.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepos extends JpaRepository<Cart, Integer> {
    @Query(value = "select c from Cart c where c.customer = ?1")
    Optional<Cart> getByUser(Users user);
}
