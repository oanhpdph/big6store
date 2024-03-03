package com.poly.service.Impl;

import com.poly.dto.UserDetailDto;
import com.poly.entity.Users;
import com.poly.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private  final CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> userInfo = customerRepository.findByUsername(username) ;
        return userInfo.map(UserDetailDto::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));
    }
}
