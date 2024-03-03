package com.poly.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "group_product")
    private GroupProduct groupProduct;

    @Column(name = "name_product")
    private String nameProduct;

    @Column(name = "sku")
    private String sku;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "avg_point")
    private float avgPoint;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "id_brand")
    private Brand brand;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_create")
    private Date createDate;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Evaluate> listEvaluate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(mappedBy = "product")
    private List<ProductDetail> productDetails;

    @OneToMany(mappedBy = "product")
    private List<ProductFieldValue> productFieldValues;
}