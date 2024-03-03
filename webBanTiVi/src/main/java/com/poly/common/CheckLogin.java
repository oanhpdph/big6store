package com.poly.common;

import com.poly.dto.UserDetailDto;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CheckLogin {

    public UserDetailDto checkLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserDetailDto customerUserDetail = (UserDetailDto) userDetails;
            return customerUserDetail;
        }
        return null;
    }
}
