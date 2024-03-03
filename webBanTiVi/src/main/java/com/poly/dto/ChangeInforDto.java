package com.poly.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeInforDto {


    private String name;

    private String password;

    private String avatar;

    private String roles;

    @NotBlank(message="(*)Số điện thoại không được bỏ trống!")
    private String phone;

    @NotBlank(message="(*)Email không được bỏ trống!")
    @Email(message="(*)Email không hợp lệ!")
    private String email;

    private String address;

    @Past(message="(*)Ngày sinh không được bỏ trống!")
    private Date birthday;

    private boolean gender;

}
