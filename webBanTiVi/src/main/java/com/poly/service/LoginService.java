package com.poly.service;

import com.poly.dto.ChangeInforDto;
import com.poly.entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public interface LoginService {

    void login(HttpServletRequest request, Model model);

    void AddCustomer(Model model, Users customer, HttpSession httpSession);

    void comfirmRegister(HttpSession httpSession, String code,
                         RedirectAttributes redirectAttributes);

    void changeInfo( ChangeInforDto changeInforDto,
                      MultipartFile file,RedirectAttributes redirectAttributes);
    void resetPassword(Model model,String  email,RedirectAttributes redirectAttributes );
}
