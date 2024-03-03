package com.poly.entity;

import com.poly.entity.idClass.CartProductId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(CartProductId.class)
public class CartProduct {
    @Id
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductDetail product;

    @Id
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "note")
    private String note;


}
