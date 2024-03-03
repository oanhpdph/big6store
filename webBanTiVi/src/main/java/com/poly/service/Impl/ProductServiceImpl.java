package com.poly.service.Impl;

import com.poly.dto.Attribute;
import com.poly.dto.ProductDetailDto;
import com.poly.entity.Image;
import com.poly.entity.Product;
import com.poly.entity.ProductDetail;
import com.poly.entity.ProductFieldValue;
import com.poly.repository.CouponRepository;
import com.poly.repository.ProductDetailRepo;
import com.poly.repository.ProductRepository;
import com.poly.service.ProductFieldValueService;
import com.poly.service.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    private ProductDetailRepo productDetailRepo;

    @Autowired
    private ProductFieldValueService productFieldValueService;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void delete(Integer id) {
        productRepository.delete(productRepository.findById(id).get());
    }

    @Override
    public Page<Product> findAll(ProductDetailDto productDetailDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> productCriteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> productRoot = productCriteriaQuery.from(Product.class);
        List<Predicate> list = new ArrayList<Predicate>();
        if (productDetailDto.getSku() != "") {
            list.add(criteriaBuilder.equal(productRoot.get("sku"), productDetailDto.getSku()));
        }
        if (productDetailDto.getGroup() != 0) {
            list.add(criteriaBuilder.equal(productRoot.get("groupProduct").get("id"), productDetailDto.getGroup()));
        }

        list.add(criteriaBuilder.greaterThanOrEqualTo(productRoot.get("avgPoint"), productDetailDto.getPoint()));

        if (productDetailDto.getKey() != null && productDetailDto.getKey().trim().length() != 0) {
            Join<Product, ProductFieldValue> fieldValueJoin = productRoot.joinList("productFieldValues");
            list.add(criteriaBuilder.or(
                    criteriaBuilder.like(fieldValueJoin.get("value"), "%" + productDetailDto.getKey() + "%")
                    , criteriaBuilder.like(productRoot.get("nameProduct"), "%" + productDetailDto.getKey() + "%")
                    , criteriaBuilder.like(productRoot.get("sku"), "%" + productDetailDto.getKey() + "%")
            ));
        }

        if (productDetailDto.getListBrand() != null && !productDetailDto.getListBrand().isEmpty()) {
            Predicate[] predicates = productDetailDto.getListBrand().stream()
                    .map(id -> criteriaBuilder.equal(productRoot.get("brand").get("id"), id))
                    .toArray(Predicate[]::new);
            list.add(criteriaBuilder.or(predicates));
        }

        if (productDetailDto.isActive()) {
            list.add(criteriaBuilder.equal(productRoot.get("active"), true));
        }
        if (productDetailDto.getSort() == 1) {
            productCriteriaQuery.orderBy(criteriaBuilder.desc(productRoot.get("createDate")));
        }
        if (productDetailDto.getSort() == 2) {
            productCriteriaQuery.orderBy(criteriaBuilder.asc(productRoot.get("createDate")));
        }
        if (productDetailDto.getSort() == 3) {
            productCriteriaQuery.orderBy(criteriaBuilder.desc(productRoot.get("avgPoint")));
        }
        if (productDetailDto.getSort() == 4) {
            productCriteriaQuery.orderBy(criteriaBuilder.asc(productRoot.get("avgPoint")));
        }

        productCriteriaQuery.select(productRoot).distinct(true).where(criteriaBuilder.and(list.toArray(new Predicate[list.size()])));

        Pageable pageable = PageRequest.of(productDetailDto.getPage()-1, productDetailDto.getSize());

        List<Product> result = entityManager.createQuery(productCriteriaQuery).setMaxResults(pageable.getPageSize()).setFirstResult((int) pageable.getOffset()).getResultList();
        List<Product> result1 = entityManager.createQuery(productCriteriaQuery).getResultList();
        Page<Product> page = new PageImpl<>(result, pageable, result1.size());

        return page;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Integer id) {
        return productRepository.findById(id).get();
    }

    @Override
    public Product update(Integer id, ProductDetailDto product) {
        Product product1 = findById(id);
        if (findById(id) != null) {
            if (product.isActive() == false) {
                product1.setActive(product.isActive());
                for (ProductDetail productDetail : product1.getProductDetails()) {
                    productDetail.setActive(false);
                    productDetailRepo.save(productDetail);
                }
            } else {
                product1.setActive(product.isActive());
            }
            if (product.getProduct() != null) {
                List<ProductFieldValue> productFieldValue = productFieldValueService.findByProduct(product1);
                if (!productFieldValue.isEmpty()) {
                    for (int i = 0; i < productFieldValue.size(); i++) {
                        for (Attribute attribute : product.getProduct()) {
                            if (attribute.getId() == productFieldValue.get(i).getField().getId()) {
                                if (attribute.getValue().trim().length() != 0) {
                                    productFieldValue.get(i).setValue(attribute.getValue());
                                    productFieldValueService.save(productFieldValue.get(i));
                                }
                            }
                        }
                    }
                }
            }
            productRepository.save(product1);
        }
        return null;
    }
    @Override
    public List<ProductDetailDto> getProduct(List<ProductDetailDto> listReturn, List<Product> listInput) {
        for (Product product : listInput) {
            ProductDetailDto productDetailDto1 = new ProductDetailDto();
            productDetailDto1.setNameProduct(product.getNameProduct() + " " + product.getSku());
            //name
            int temp = 0;
            if (product.getActive() == true) {
                for (ProductDetail productDetail : product.getProductDetails()) {
                    if (productDetail.isActive()) {
                        for (Image image : productDetail.getListImage()) {
                            if (image.isLocation()) {
                                productDetailDto1.setImage(image.getLink());
                                // ảnh
                                break;
                            }
                        }
                        if (productDetail.getCoupon() != null && productDetail.getCoupon().isActive() && ((LocalDate.now().isAfter(productDetail.getCoupon().getDateStart().toLocalDate()) || LocalDate.now().isEqual(productDetail.getCoupon().getDateStart().toLocalDate())) && (LocalDate.now().isBefore(productDetail.getCoupon().getDateEnd().toLocalDate()) || LocalDate.now().isEqual(productDetail.getCoupon().getDateEnd().toLocalDate())))) {
                            BigDecimal reduceMoney = productDetail.getPriceExport().multiply(BigDecimal.valueOf(Integer.parseInt(productDetail.getCoupon().getValue()))).divide(BigDecimal.valueOf(100));
                            productDetailDto1.setReduceMoney(productDetail.getPriceExport().subtract(reduceMoney));
                            //giá sau giảm
                            productDetailDto1.setPrice(productDetail.getPriceExport());
                            // giá trước giảm
                        } else {
                            productDetailDto1.setPrice(BigDecimal.valueOf(0));
                            productDetailDto1.setReduceMoney(productDetail.getPriceExport());
                        }

                        productDetailDto1.setId(productDetail.getId());
                        productDetailDto1.setPoint(product.getAvgPoint());
                        productDetailDto1.setQuantityEvalute(product.getListEvaluate().stream().filter(evaluate -> evaluate.isActive() == true).toList().size());
                        temp = 1;
                        break;
                    }
                }
                if (temp == 1) {
                    listReturn.add(productDetailDto1);
                }
            }
        }
        return listReturn;
    }

    @Override
    public BigDecimal getReduceMoney(ProductDetail product){
        BigDecimal reduceMoney = BigDecimal.valueOf(0);
        if (product.getCoupon() != null && product.getCoupon().isActive()
                && ((LocalDate.now().isAfter(product.getCoupon().getDateStart().toLocalDate()) || (LocalDate.now().isEqual(product.getCoupon().getDateStart().toLocalDate())))
                && ((LocalDate.now().isBefore(product.getCoupon().getDateEnd().toLocalDate()) || LocalDate.now().isEqual(product.getCoupon().getDateEnd().toLocalDate()))))) {
            reduceMoney = product.getPriceExport().subtract(product.getPriceExport().multiply(BigDecimal.valueOf(Double.parseDouble(product.getCoupon().getValue())).divide(new BigDecimal(100))));
        }
        return reduceMoney;
    }
}
