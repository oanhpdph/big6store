package com.poly.repository;

import com.poly.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByUsername(String username);

    Users getCustomerByName(String username);
//    Customer findByEmail(String email);

    @Query(value = "select c from Users c where c.email=?1")
    Optional<Users> checkEmail(String email);

    @Query(value = "select c from Users c where c.id=?1")
    Optional<Users> findId(Integer id);

}
