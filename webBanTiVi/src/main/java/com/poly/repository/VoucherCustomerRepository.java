package com.poly.repository;

import com.poly.entity.Users;
import com.poly.entity.Voucher;
import com.poly.entity.VoucherCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface VoucherCustomerRepository extends JpaRepository<VoucherCustomer, Integer> {
    @Query("select o from VoucherCustomer o where o.customer.id=?1 and o.voucher.id=?2")
    VoucherCustomer findByCustomerAndVoucher(Integer idcus, Integer idvou);

    @Query("select o from VoucherCustomer o where o.voucher.id=?1")
    List<VoucherCustomer> findAllByVoucher(Integer id);

    @Query("select o from VoucherCustomer o where o.voucher.id=?2 and " +
            "(o.customer.name like ?1 or o.customer.email like ?1 " +
            "or o.customer.phoneNumber like ?1)")
    List<VoucherCustomer> findAllByKeyword(String keyword, Integer id);

    @Transactional
    @Modifying
    @Query("UPDATE VoucherCustomer o SET o.voucher = :voucher, o.customer = :customer, o.active = :active WHERE o.id = :id")
    void updateById(@Param("voucher") Voucher voucher, @Param("customer") Users users, @Param("active") boolean active, @Param("id") int id);

    @Query(value = "select v from VoucherCustomer v where  v.customer=?1")
    List<VoucherCustomer> findByUser(Users users);

}
