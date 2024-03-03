package com.poly.repository;

import com.poly.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepos extends JpaRepository<Bill, Integer> {
    @Query(value = "select  b from Bill b  where b.paymentDate =?1")
    List<Bill> findBillByDate(Date date);

    @Query(value = "select * from Bill b where b.id=?1", nativeQuery = true)
    Optional<Bill> findByBill(Integer id);

    @Query(value = "select  b from Bill b  where b.customer.id =?1")
    List<Bill> findBillByUser(Integer id);

    @Query(value = "select b from Bill b where b.billStatus.code=?1")
    List<Bill> findBillReturn(String codeStt);

    @Query(value = "select b from Bill b where b.code=?1")
    Optional<Bill> findByCode(String code);

    @Query(value = "select  b from Bill b  where b.customer.id =?1 and b.billStatus.id=?2")
    List<Bill> findBillByUserAndStatus(Integer idUser,Integer idStatus);

}
