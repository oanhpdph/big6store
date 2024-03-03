package com.poly.service.Impl;

import com.poly.dto.Attribute;
import com.poly.dto.ImageDto;
import com.poly.dto.ProductDetailDto;
import com.poly.dto.ProductDetailListDto;
import com.poly.entity.*;
import com.poly.repository.FieldRepo;
import com.poly.repository.ProductDetailRepo;
import com.poly.service.*;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProductDetailImpl implements ProductDetailService {
    @Autowired
    private ProductDetailRepo productDetailRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductFieldValueService productFieldValueService;

    @Autowired
    private ProductDetailFieldService productDetailFieldService;

    @Autowired
    private FieldRepo fieldService;

    @Autowired
    private GroupProductService groupProductService;

    @PersistenceContext
    private EntityManager entityManager;

    public void addProductDiscount(Coupon coupon, Integer id) {
        productDetailRepo.addProductDiscount(coupon, id);
    }


    public List<ProductDetail> findBySku(String keyword) {
        return productDetailRepo.findbySku(keyword);
    }

    @Override
    public Product saveList(ProductDetailDto dto) {
        Product product = new Product();
        product.setGroupProduct(groupProductService.findById(dto.getGroup()));
        product.setNameProduct(dto.getNameProduct());
        product.setSku(dto.getSku());
        product.setBrand(brandService.findById(dto.getBrand()));
        if (!dto.getListProduct().isEmpty()) {
            product.setActive(true);
        } else {
            product.setActive(false);
        }
        product.setCreateDate(new Date());
        product.setAvgPoint(0);
        productService.save(product);
        for (int i = 0; i < dto.getProduct().size(); i++) {
            ProductFieldValue productFieldValue = new ProductFieldValue();
            productFieldValue.setField(fieldService.findById(dto.getProduct().get(i).getId()).get());
            productFieldValue.setValue(dto.getProduct().get(i).getValue());
            productFieldValue.setProduct(product);
            productFieldValueService.save(productFieldValue);
        }

        for (ProductDetailListDto productFieldValue : dto.getListProduct()) {
            ProductDetail productDetail = new ProductDetail();
            productDetail.setQuantity(productFieldValue.getQuantity());
            productDetail.setPriceExport(productFieldValue.getPriceExport());
            productDetail.setPriceImport(productFieldValue.getPriceImport());
            productDetail.setActive(productFieldValue.isActive());
            productDetail.setProduct(product);
            productDetail.setCreateDate(new Date());
            productDetail.setSku(productFieldValue.getSku());
            productDetailRepo.save(productDetail);

            for (Attribute attribute : productFieldValue.getListAttributes()) {
                ProductDetailField productDetailField = new ProductDetailField();
                productDetailField.setField(fieldService.findById(attribute.getId()).get());
                productDetailField.setProductDetail(productDetail);
                productDetailField.setValue(attribute.getValue());
                productDetailFieldService.save(productDetailField);
            }
            for (ImageDto imageDto : productFieldValue.getImage()) {
                Image image = new Image();
                image.setLocation(imageDto.getLocation());
                image.setProduct(productDetail);
                image.setLink(imageDto.getMultipartFile());
                imageService.add(image);
            }
        }
        return product;
    }


    @Override
    public ProductDetail findById(Integer id) {
        Optional<ProductDetail> optional = productDetailRepo.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }


    @Override
    public Page<ProductDetail> findAll(ProductDetailListDto productDetailListDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<ProductDetail> productCriteriaQuery = criteriaBuilder.createQuery(ProductDetail.class);
        Root<ProductDetail> productDetailRoot = productCriteriaQuery.from(ProductDetail.class);
        List<Predicate> list = new ArrayList<Predicate>();

        if (!productDetailListDto.getSku().isEmpty()) {
            list.add(criteriaBuilder.equal(productDetailRoot.get("sku"), productDetailListDto.getSku()));
        }
        if (productDetailListDto.getKey() != null && productDetailListDto.getKey().trim().length() != 0) {
            Join<ProductDetail, ProductDetailField> fieldValueJoin = productDetailRoot.joinList("fieldList");
            list.add(criteriaBuilder.or(
                    criteriaBuilder.like(fieldValueJoin.get("value"), "%" + productDetailListDto.getKey().trim() + "%"),
                    criteriaBuilder.like(productDetailRoot.get("product").get("nameProduct"), "%" + productDetailListDto.getKey().trim() + "%"),
                    criteriaBuilder.like(productDetailRoot.get("sku"), "%" + productDetailListDto.getKey().trim() + "%")
            ));
        }
        if (productDetailListDto.getSort() == 1) {
            productCriteriaQuery.orderBy(criteriaBuilder.desc(productDetailRoot.get("createDate")));
        } else {
            productCriteriaQuery.orderBy(criteriaBuilder.asc(productDetailRoot.get("createDate")));
        }

        productCriteriaQuery.select(productDetailRoot).distinct(true).where(criteriaBuilder.and(list.toArray(new Predicate[list.size()])));
        Pageable pageable = PageRequest.of(productDetailListDto.getPage() - 1, productDetailListDto.getSize());

        List<ProductDetail> result = entityManager.createQuery(productCriteriaQuery).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        List<ProductDetail> result2 = entityManager.createQuery(productCriteriaQuery).getResultList();

        Page<ProductDetail> page = new PageImpl<>(result, pageable, result2.size());
        return page;
    }

    @Override
    public List<ProductDetail> findAll() {
        return productDetailRepo.findAll();
    }

    @Override
    public Boolean delete(Integer id) {
        ProductDetail productDetail = findById(id);
        if (productDetail != null) {
            productDetailRepo.delete(productDetail);
            return true;
        }
        return false;
    }

    @Override
    public ProductDetail update(ProductDetailListDto productDetailListDto) {
        ProductDetail productDetail = findById(productDetailListDto.getId());
        if (productDetail != null) {
            productDetail.setActive(productDetailListDto.isActive());
            if (productDetailListDto.getPriceExport() != null && productDetailListDto.getPriceExport().compareTo(BigDecimal.valueOf(0)) >= 0) {
                productDetail.setPriceExport(productDetailListDto.getPriceExport());
            }
            if (productDetailListDto.getPriceImport() != null && productDetailListDto.getPriceImport().compareTo(BigDecimal.valueOf(0)) >= 0) {
                productDetail.setPriceImport(productDetailListDto.getPriceImport());
            }
            if (productDetailListDto.getQuantity() != null && productDetailListDto.getQuantity() >= 0) {
                productDetail.setQuantity(productDetailListDto.getQuantity());
            }
            if (productDetailListDto.getSku().trim().length() != 0) {
                productDetail.setSku(productDetailListDto.getSku());
            }
            if (productDetailListDto.getImage() != null) {
                for (ImageDto imageDto : productDetailListDto.getImage()) {
                    if (imageDto.getMultipartFile().trim().length() != 0) {
                        Image image = imageService.findById(imageDto.getId());
                        image.setLink(imageDto.getMultipartFile());
                        imageService.add(image);
                    }
                }
            }
            productDetailRepo.save(productDetail);
        }
        return productDetail;
    }

    @Override
    public ProductDetail save(ProductDetailDto productDetailDto) {
        Product product = productService.findById(productDetailDto.getId());
        if (validate(productDetailDto) == false) {
            return null;
        }
        for (ProductDetailListDto productDetailListDto : productDetailDto.getListProduct()) {
            ProductDetail productDetail = new ProductDetail();
            productDetail.setActive(true);
            productDetail.setQuantity(productDetailListDto.getQuantity());
            productDetail.setPriceExport(productDetailListDto.getPriceExport());
            productDetail.setPriceImport(productDetailListDto.getPriceImport());
            productDetail.setProduct(product);
            productDetail.setCreateDate(new Date());
            productDetail.setSku(productDetailListDto.getSku());
            productDetail = productDetailRepo.save(productDetail);
            for (ImageDto imageDto : productDetailListDto.getImage()) {
                Image image = new Image();
                image.setLink(imageDto.getMultipartFile());
                image.setLocation(imageDto.getLocation());
                image.setProduct(productDetail);
                imageService.add(image);
            }
            for (Attribute attribute : productDetailListDto.getListAttributes()) {
                Optional<Field> field = fieldService.findById(attribute.getId());
                if (field.isPresent()) {
                    ProductDetailField productDetailField = new ProductDetailField();
                    productDetailField.setValue(attribute.getValue());
                    productDetailField.setField(field.get());
                    productDetailField.setProductDetail(productDetail);
                    productDetailFieldService.save(productDetailField);
                }
            }
            return productDetail;
        }
        return null;
    }

    public boolean validate(ProductDetailDto productDetailDto) {
        for (ProductDetailListDto productDetailListDto : productDetailDto.getListProduct()) {

            if (productDetailListDto.getQuantity() == null || productDetailListDto.getQuantity() < 0) {
                return false;
            }
            if (productDetailListDto.getPriceExport() == null || productDetailListDto.getPriceExport().compareTo(BigDecimal.valueOf(0)) < 0) {
                return false;
            }
            if (productDetailListDto.getPriceImport() == null || productDetailListDto.getPriceImport().compareTo(BigDecimal.valueOf(0)) < 0) {
                return false;
            }
            if (productDetailListDto.getSku() == null || productDetailListDto.getSku().trim().length() == 0) {
                return false;
            }

        }
        return true;
    }

    public void deleteProductDiscount(Integer id) {
        productDetailRepo.deleteProductDiscount(id);
    }
}
