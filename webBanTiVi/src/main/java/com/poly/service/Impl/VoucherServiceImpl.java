package com.poly.service.Impl;

import com.poly.dto.SearchVoucherDto;
import com.poly.entity.Voucher;
import com.poly.repository.VoucherRepository;
import com.poly.service.VoucherService;
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
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    VoucherRepository voucherRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Voucher> loadData(SearchVoucherDto searchVoucherDto, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Voucher> voucherCriteriaQuery = criteriaBuilder.createQuery(Voucher.class);
        Root<Voucher> voucherRoot = voucherCriteriaQuery.from(Voucher.class);

        List<Predicate> list = new ArrayList<Predicate>();
        if (!searchVoucherDto.getKey().isEmpty()) {
            list.add(criteriaBuilder.or(
                    criteriaBuilder.like(voucherRoot.get("code"),"%"+ searchVoucherDto.getKey()+"%"),
                    criteriaBuilder.like(voucherRoot.get("nameVoucher"),"%"+ searchVoucherDto.getKey()+"%")));
        }
        if (!searchVoucherDto.getDate().isEmpty()) {
            String date1 = searchVoucherDto.getDate().substring(0, searchVoucherDto.getDate().indexOf("-") - 1).replace("/", "-");
            String date2 = searchVoucherDto.getDate().substring(searchVoucherDto.getDate().indexOf("-") + 1, searchVoucherDto.getDate().length()).replace("/", "-");
            java.sql.Date dateStart = java.sql.Date.valueOf(date1.trim());
            java.sql.Date dateEnd = java.sql.Date.valueOf(date2.trim());
            list.add(criteriaBuilder.and(criteriaBuilder.greaterThan(voucherRoot.get("startDay"), dateStart),
                    criteriaBuilder.lessThan(voucherRoot.get("expirationDate"), dateEnd)));
        }
        voucherCriteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[list.size()])));
        List<Voucher> result = entityManager.createQuery(voucherCriteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        List<Voucher> result2 = entityManager.createQuery(voucherCriteriaQuery).getResultList();
        Page<Voucher> page = new PageImpl<>(result, pageable, result2.size());
        return page;
    }

    @Override
    public Voucher save(Voucher voucher) {
        return this.voucherRepository.save(voucher);
    }

    @Override
    public void delete(Integer id) {
        this.voucherRepository.deleteById(id);
    }

    @Override
    public Page<Voucher> findAll(Pageable pageable) {
        return (Page<Voucher>) this.voucherRepository.findAll(pageable).toList();
    }

    @Override
    public Optional<Voucher> findById(Integer id) {

        return this.voucherRepository.findById(id);
    }

    @Override
    public List<Voucher> findAllList() {
        return this.voucherRepository.findAll();
    }

    public List<Voucher> findAllByDate(Date date) {
        return voucherRepository.findAllByExpirationDate(date);
    }
}
