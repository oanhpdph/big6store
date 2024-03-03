package com.poly.service.Impl;

import com.poly.dto.SearchVoucherDto;
import com.poly.dto.VoucherCustomerRes;
import com.poly.entity.Users;
import com.poly.entity.Voucher;
import com.poly.entity.VoucherCustomer;
import com.poly.repository.CustomerRepository;
import com.poly.repository.VoucherCustomerRepository;
import com.poly.repository.VoucherRepository;
import com.poly.service.VoucherCustomerService;
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

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherCustomerServiceImpl implements VoucherCustomerService {
    @Autowired
    VoucherCustomerRepository voucherCustomerRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    VoucherRepository voucherRepository;

    public List<VoucherCustomer> findAllByVoucher(Integer id) {
        return voucherCustomerRepository.findAllByVoucher(id);
    }

    public List<VoucherCustomer> findAllByKeyword(String keyword, Integer id) {
        return voucherCustomerRepository.findAllByKeyword(keyword, id);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void updateById(VoucherCustomerRes voucher, int id) {
        Optional<Users> optionalCustomer = customerRepository.findById(voucher.getCustomer());
        Optional<Voucher> optionalVoucher = voucherRepository.findById(voucher.getVoucher());
        VoucherCustomer voucherCustomer = new VoucherCustomer();
        voucherCustomer.setCustomer(optionalCustomer.get());
        voucherCustomer.setVoucher(optionalVoucher.get());
        voucherCustomer.setActive(voucher.getActive());
        voucherCustomerRepository.updateById(optionalVoucher.get(), optionalCustomer.get(), voucher.getActive(), id);
    }

    @Override
    public VoucherCustomer save(VoucherCustomerRes voucher) {
        Optional<Users> optionalCustomer = customerRepository.findById(voucher.getCustomer());
        Optional<Voucher> optionalVoucher = voucherRepository.findById(voucher.getVoucher());
        VoucherCustomer voucherCustomer = new VoucherCustomer();
        voucherCustomer.setCustomer(optionalCustomer.get());
        voucherCustomer.setVoucher(optionalVoucher.get());
        voucherCustomer.setActive(voucher.getActive());
        return this.voucherCustomerRepository.save(voucherCustomer);

    }

    @Override
    public void delete(Integer id) {
        this.voucherCustomerRepository.deleteById(id);
    }

    @Override
    public List<VoucherCustomer> findByUser(Integer user) {
        Optional<Users> optional = customerRepository.findById(user);
        if (optional.isPresent()) {
            List<VoucherCustomer> voucherCustomers = voucherCustomerRepository.findByUser(optional.get());
            return voucherCustomers;
        }
        return null;
    }

    @Override
    public Optional<VoucherCustomer> findById(Integer id) {
        return this.voucherCustomerRepository.findById(id);
    }

    @Override
    public Page<VoucherCustomer> loadData(SearchVoucherDto searchVoucherDto, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<VoucherCustomer> voucherCustomerCriteriaQuery = criteriaBuilder.createQuery(VoucherCustomer.class);
        Root<VoucherCustomer> voucherCustomerRoot = voucherCustomerCriteriaQuery.from(VoucherCustomer.class);

        List<Predicate> list = new ArrayList<Predicate>();
        if (!searchVoucherDto.getKey().isEmpty()) {
            list.add(criteriaBuilder.or(
                    criteriaBuilder.equal(voucherCustomerRoot.get("customer"), searchVoucherDto.getKey()),
                    criteriaBuilder.equal(voucherCustomerRoot.get("voucher"), searchVoucherDto.getKey())));
        }
        if (!searchVoucherDto.getDate().isEmpty()) {
            String date1 = searchVoucherDto.getDate().substring(0, searchVoucherDto.getDate().indexOf("-") - 1).replace("/", "-");
            String date2 = searchVoucherDto.getDate().substring(searchVoucherDto.getDate().indexOf("-") + 1, searchVoucherDto.getDate().length()).replace("/", "-");
            System.out.println(date1 + date2);
            Date dateStart = Date.valueOf(date1.trim());
            Date dateEnd = Date.valueOf(date2.trim());
            list.add(criteriaBuilder.and(criteriaBuilder.greaterThan(voucherCustomerRoot.get("dateStart"), dateStart),
                    criteriaBuilder.lessThan(voucherCustomerRoot.get("dateEnd"), dateEnd)));
        }
        voucherCustomerCriteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[list.size()])));
        List<VoucherCustomer> result = entityManager.createQuery(voucherCustomerCriteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        List<VoucherCustomer> result2 = entityManager.createQuery(voucherCustomerCriteriaQuery).getResultList();
        Page<VoucherCustomer> page = new PageImpl<>(result, pageable, result2.size());
        return page;
    }
}
