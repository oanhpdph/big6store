package com.poly.repository;


import com.poly.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher,Integer> {
    @Query(value = "select * from Voucher b where b.id like %?1%", nativeQuery = true)
    Optional<Voucher> getVoucherByName(Integer id);
    @Query(value = "select b from Voucher b where b.id = ?1 ")
    Optional<Voucher> getVoucherById(Integer id);
    @Query("select b from Voucher b where b.expirationDate>=?1 and b.active=true ")
    List<Voucher> findAllByExpirationDate(Date date);
}
