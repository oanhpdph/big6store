package com.poly.repository;

import com.poly.entity.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillStatusRepos extends JpaRepository<BillStatus,Integer> {

    @Query(value = "select bs from BillStatus bs where bs.code = ?1")
    Optional<BillStatus> findByCode(String code);
}
