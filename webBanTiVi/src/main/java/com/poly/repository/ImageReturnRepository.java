package com.poly.repository;

import com.poly.entity.ImageReturned;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageReturnRepository extends JpaRepository<ImageReturned,Integer > {
}
