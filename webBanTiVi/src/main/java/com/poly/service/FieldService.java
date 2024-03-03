package com.poly.service;

import com.poly.dto.Attribute;
import com.poly.entity.Field;
import org.springframework.data.domain.Page;

public interface FieldService {
    Page<Field> findAll(Attribute attribute);

    Field findById(Integer id);

    Field save(Field field);

    Field update(Field field);

}
