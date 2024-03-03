package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "image_returned")
@AllArgsConstructor
@NoArgsConstructor
public class ImageReturned {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_history_billProduct")
    private HistoryBillProduct hBillProduct;

    @Column(name = "name_image")
    private String nameImage;

    @Override
    public String toString(){
        return "ImageReturned{" +
                "id=" + id +
                ", nameImage=" + nameImage +
                '}';
    }
}
