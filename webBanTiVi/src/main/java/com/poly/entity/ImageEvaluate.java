package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image_evaluate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageEvaluate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="id_evaluate")
    private Evaluate evaluate;

    @Column(name="name_image")
    private String nameImage;
}
