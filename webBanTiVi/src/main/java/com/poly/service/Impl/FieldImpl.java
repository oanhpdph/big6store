package com.poly.service.Impl;

import com.poly.dto.Attribute;
import com.poly.dto.ProductDetailListDto;
import com.poly.entity.Field;
import com.poly.entity.ProductDetailField;
import com.poly.repository.FieldRepo;
import com.poly.service.FieldService;
import com.poly.service.ProductDetailFieldService;
import com.poly.service.ProductDetailService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FieldImpl implements FieldService {

    @Autowired
    private FieldRepo fieldRepo;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ProductDetailFieldService productDetailFieldService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Field> findAll(Attribute attribute) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Field> fieldCriteriaQuery = criteriaBuilder.createQuery(Field.class);
        Root<Field> fieldRoot = fieldCriteriaQuery.from(Field.class);
        List<Predicate> list = new ArrayList<Predicate>();

        if (attribute.isActive()) {
            list.add(criteriaBuilder.equal(fieldRoot.get("active"), true));
        }
        fieldCriteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[list.size()])));
        List<Field> result2 = entityManager.createQuery(fieldCriteriaQuery).getResultList();
        Pageable pageable;
        if (!result2.isEmpty()) {
            pageable = PageRequest.of(attribute.getPage() - 1, result2.size());
        } else {
            pageable = PageRequest.of(attribute.getPage() - 1, 1);
        }


        List<Field> result = entityManager.createQuery(fieldCriteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        Page<Field> page = new PageImpl<>(result, pageable, result2.size());
        return page;
    }

    @Override
    public Field findById(Integer id) {
        return fieldRepo.findById(id).get();
    }

    @Override
    public Field save(Field field) {
        return fieldRepo.save(field);
    }

    @Override
    public Field update(Field field) {
        if (field.isActive() == false && field.isVariant()) {
            List<ProductDetailField> list = productDetailFieldService.findByField(field);
            for (ProductDetailField productDetailField : list) {
                productDetailField.getProductDetail();
                ProductDetailListDto productDetailListDto = new ProductDetailListDto();
                productDetailListDto.setActive(false);
                productDetailListDto.setId(productDetailField.getId());
                productDetailService.update(productDetailListDto);
            }
        }
        return fieldRepo.save(field);
    }
}
