package com.poly.service.Impl;

import com.poly.dto.EvaluateRes;
import com.poly.entity.Evaluate;
import com.poly.entity.ImageEvaluate;
import com.poly.entity.Product;
import com.poly.entity.Users;
import com.poly.repository.*;
import com.poly.service.EvaluateService;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EvaluateServiceImpl implements EvaluateService {

    @Autowired
    private EvaluateRepos evaluateRepos;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductDetailRepo detailRepo;

    @Autowired
    private ImageEvaluateRepo imageEvaluateRepo;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<Evaluate> getAll(EvaluateRes evaluateRes) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Evaluate> evaluateCriteriaQuery = criteriaBuilder.createQuery(Evaluate.class);
        Root<Evaluate> evaluateRoot = evaluateCriteriaQuery.from(Evaluate.class);
        List<Predicate> list = new ArrayList<Predicate>();

        if (evaluateRes.getProduct() != null) {
            list.add(criteriaBuilder.equal(evaluateRoot.get("product").get("id"), evaluateRes.getProduct()));
        }

        evaluateCriteriaQuery.orderBy(criteriaBuilder.desc(evaluateRoot.get("dateCreate")));

        evaluateCriteriaQuery.where(criteriaBuilder.and(list.toArray(new Predicate[list.size()])));


        Pageable pageable = PageRequest.of(evaluateRes.getPage() - 1, evaluateRes.getSize());

        List<Evaluate> result = entityManager.createQuery(evaluateCriteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        List<Evaluate> result2 = entityManager.createQuery(evaluateCriteriaQuery).getResultList();

        Page<Evaluate> page = new PageImpl<>(result, pageable, result2.size());
        return page;
    }

    @Override
    public Evaluate add(EvaluateRes evaluate) {
        Optional<Users> optionalCustomer = customerRepository.findById(evaluate.getCustomer());
        Optional<Product> optionalProduct = productRepository.findById(evaluate.getProduct());
        Evaluate eval = new Evaluate();
        if (optionalCustomer.isPresent()) {
            eval.setCustomer(optionalCustomer.get());
        }
        if (optionalProduct.isPresent()) {
            eval.setProduct(optionalProduct.get());
        }

        eval.setDateCreate(new Date());
        eval.setPoint(evaluate.getPoint());
        eval.setComment(evaluate.getComment());
        eval.setActive(evaluate.isActive());
        eval = this.evaluateRepos.save(eval);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            Optional<Float> avgPoint = evaluateRepos.avgPoint(optionalProduct.get());
            if (avgPoint.isPresent()) {
                float roundedNumber = (float) (Math.round(avgPoint.get() * 10.0) / 10.0);
                product.setAvgPoint(roundedNumber);
                productRepository.save(product);
            }
        }
        for (String s : evaluate.getImage()) {
            ImageEvaluate imageEvaluate = new ImageEvaluate();
            imageEvaluate.setEvaluate(eval);
            imageEvaluate.setNameImage(s);
            imageEvaluateRepo.save(imageEvaluate);
        }

        return eval;
    }

    @Override
    public Evaluate findById(Integer id) {
        Optional<Evaluate> evaluate = evaluateRepos.findById(id);
        if (evaluate.isPresent()) {
            return evaluate.get();
        }
        return null;
    }

    @Override
    public Evaluate update(EvaluateRes evaluateRes) {
        Evaluate evaluate = findById(evaluateRes.getId());
        if (evaluate != null) {
            evaluate.setActive(evaluateRes.isActive());
            evaluateRepos.save(evaluate);

            Optional<Float> avgPoint = evaluateRepos.avgPoint(evaluate.getProduct());
            float roundedNumber;
            if (avgPoint.isPresent()) {
                roundedNumber = (float) (Math.round(avgPoint.get() * 10.0) / 10.0);
                evaluate.getProduct().setAvgPoint(roundedNumber);

            } else {
                evaluate.getProduct().setAvgPoint(0);
            }
            productRepository.save(evaluate.getProduct());
        }
        return null;
    }
}
