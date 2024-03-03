package com.poly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "bill")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="id_user")
    private Users customer;

    @Column(name="code")
    private String code;

    @Temporal(TemporalType.DATE)
    @Column(name="create_date")
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column(name="payment_date")
    private Date paymentDate;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name="payment_status")
    private int paymentStatus;

    @ManyToOne
    @JoinColumn(name="id_status")
    private BillStatus billStatus;

    @ManyToOne
    @JoinColumn(name="id_paymentmethod")
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name="id_voucher")
    private Voucher voucher;

    @Column(name="voucher_value")
    private BigDecimal voucherValue;

    @Column(name="note")
    private String note;

    @OneToMany(mappedBy = "bill",fetch = FetchType.EAGER)
    private List<BillProduct> billProducts;

    @OneToMany(mappedBy = "idBill")
    private List<DeliveryNotes> deliveryNotes;

}

