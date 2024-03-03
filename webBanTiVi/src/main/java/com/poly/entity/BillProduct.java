package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "bill_product")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private ProductDetail product;

    @ManyToOne
    @JoinColumn(name = "id_bill")
    private Bill bill;

    @Column(name="quantity")
    private Integer quantity;

    @Column(name="price")
    private BigDecimal price;

    @Column(name="reduced_money")
    private BigDecimal reducedMoney;

    @Column(name="note")
    private String note;

    @Column(name="status")
    private Integer status;

}
