package com.poly.service;

import com.poly.dto.BillProRes;
import com.poly.dto.SearchStaffDto;
import com.poly.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

public interface CustomerService {

    public Users add(BillProRes customer) throws NoSuchAlgorithmException, UnsupportedEncodingException;

    Users save(Users customer);

    void deleteById(Integer id);

    List<Users> findAll();

    Optional<Users> findById(Integer id);

    Users findByEmail(String email);

    Users getCustomerByName(String username);

    Page<Users> loadData(SearchStaffDto searchStaffDto, Pageable pageable);
}
