package com.poly.dto;

import com.poly.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto implements UserDetails {

    private int id;

    private String fullName;

    private String name;

    private String password;

    private String avatar;

    private String roles;

    private String phone;

    private String email;

    private String address;

    private Date birthday;

    private boolean gender;

    private List<GrantedAuthority> authorities;

    public UserDetailDto(Users customer) {
        id=customer.getId();
        fullName = customer.getName();
        name = customer.getUsername();
        password = customer.getPassword();
        roles = customer.getRoles();
        avatar = customer.getAvatar();
        email = customer.getEmail();
        address = customer.getAddress();
        birthday = customer.getBirthday();
        phone = customer.getPhoneNumber();
        gender = customer.isGender();
        authorities = Arrays.stream(customer.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
