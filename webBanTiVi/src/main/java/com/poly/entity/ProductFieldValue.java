package com.poly.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_field_value")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFieldValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @Column(name = "value")
    private String value;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Override
    public String toString() {
        return "ProductFieldValue{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
