package com.poly.service.Impl;

import com.poly.dto.BillProRes;
import com.poly.dto.SearchStaffDto;
import com.poly.entity.Users;
import com.poly.repository.CustomerRepository;
import com.poly.service.CustomerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements CustomerService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Users add(BillProRes customer) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Users cus = new Users();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] emailHash = md5.digest(customer.getEmail().getBytes());
        byte[] phoneHash = md5.digest(customer.getPhoneNumber().getBytes());

        // Kết hợp hash của email và phone
        byte[] combinedHash = new byte[emailHash.length + phoneHash.length];
        System.arraycopy(emailHash, 0, combinedHash, 0, emailHash.length);
        System.arraycopy(phoneHash, 0, combinedHash, emailHash.length, phoneHash.length);

//         Tạo username từ kết quả kết hợp
        String username = new String(combinedHash, "UTF-8").substring(0, 8);
        cus.setUsername(username);
        cus.setName(customer.getName());
        cus.setEmail(customer.getEmail());
        cus.setPhoneNumber(customer.getPhoneNumber());
        cus.setAddress(customer.getAddress());
        cus.setPassword(passwordEncoder.encode("123"));
        cus.setBirthday(null);
        this.customerRepository.save(cus);
        return cus;
    }

    @Override
    public Users save(Users customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void deleteById(Integer id) {
        this.customerRepository.deleteById(id);
    }

    @Override
    public List<Users> findAll() {
        return this.customerRepository.findAll();
    }

    @Override
    public Optional<Users> findById(Integer id) {
        return this.customerRepository.findById(id);
    }

    @Override
    public Users findByEmail(String email) {
        Optional<Users> customer = customerRepository.checkEmail(email);
        if (customer.isPresent()) {
            return customerRepository.checkEmail(email).get();
        }
        return null;
    }

    @Override
    public Page<Users> loadData(SearchStaffDto searchStaffDto, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Users> customerCriteriaQuery = criteriaBuilder.createQuery(Users.class);
        Root<Users> customerRoot = customerCriteriaQuery.from(Users.class);

        List<Predicate> list = new ArrayList<Predicate>();
        if (searchStaffDto.getKey() != "") {
            list.add(criteriaBuilder.or(
                    criteriaBuilder.like(customerRoot.get("name"), "%" + searchStaffDto.getKey() + "%"),
                    criteriaBuilder.like(customerRoot.get("phoneNumber"), "%" + searchStaffDto.getKey() + "%")));
        }

        if (searchStaffDto.getRole().equals("USER") && !searchStaffDto.getRole().isBlank()) {
            list.add(criteriaBuilder.equal(customerRoot.get("roles"), "USER"));
        } else {
            list.add(criteriaBuilder.or(criteriaBuilder.equal(customerRoot.get("roles"), "STAFF"),
                    criteriaBuilder.equal(customerRoot.get("roles"), "ADMIN")));
        }


        customerCriteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[list.size()])));

        List<Users> result = entityManager.createQuery(customerCriteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        List<Users> result2 = entityManager.createQuery(customerCriteriaQuery).getResultList();

        Page<Users> page = new PageImpl<>(result, pageable, result2.size());
        return page;
    }

    @Override
    public Users getCustomerByName(String username) {
        return customerRepository.getCustomerByName(username);
    }

}
