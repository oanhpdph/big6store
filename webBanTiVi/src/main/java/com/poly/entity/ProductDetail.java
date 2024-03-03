package com.poly.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sku")
    private String sku;

    @Column(name = "price_import")
    private BigDecimal priceImport;

    @Column(name = "price_export")
    private BigDecimal priceExport;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "active")
    private boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_create")
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_discount")
    private Coupon coupon;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Image> listImage;

    @OneToMany(mappedBy = "productDetail",fetch = FetchType.EAGER)
    private List<ProductDetailField> fieldList;

    @Override
    public String toString() {
        return "";
    }
}
