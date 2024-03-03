package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "evaluate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Evaluate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="id_product")
    private Product product;

    @ManyToOne
    @JoinColumn(name="id_user")
    private Users customer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_create")
    private Date dateCreate;

    @Column(name="point")
    private float point;

    @Column(name="comment")
    private String comment;

    @Column(name="active")
    private boolean active;

    @OneToMany(mappedBy = "evaluate")
    private List<ImageEvaluate> evaluateList;

}
