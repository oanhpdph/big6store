package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "delivery_notes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "received")
    private String received;

    @Column(name = "received_phone")
    private String receiverPhone;

    @Column(name = "received_email")
    private String receivedEmail;

    @Column(name = "receiving_address")
    private String receivingAddress;

    @Column(name = "deliver")
    private String deliver;

    @Column(name = "delivery_phone")
    private String deliveryPhone;

    @Temporal(TemporalType.DATE)
    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "received_date")
    private Date receivedDate;

    @Column(name = "delivery_fee")
    private BigDecimal deliveryFee;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "id_bill")
    private Bill idBill;
}
