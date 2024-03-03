package com.poly.repository;

import com.poly.entity.Field;
import com.poly.entity.GroupProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupProductRepo extends JpaRepository<GroupProduct, Integer> {
}
