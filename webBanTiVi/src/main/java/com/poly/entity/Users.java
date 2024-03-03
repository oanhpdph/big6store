package com.poly.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank(message = "(*)Tên không được để trống!")
    @Column(name = "username")
    private String username;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    private Date birthday;

    @Column(name = "address")
    private String address;

    @NotBlank(message="(*)Số điện thoại không được bỏ trống!")
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotBlank(message="(*)Email không được bỏ trống!")
    @Email(message = "(*)Email không hợp lệ!")
    @Column(name = "email")
    private String email;

    @NotBlank(message="(*)Mật khẩu được bỏ trống!")
    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    private boolean gender;

    @Column(name = "avatar")
    private String avatar;

   
    @Column(name = "status")
    private boolean status;


   @Column(name = "roles")
    private String roles;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<Bill> listBill;

}

