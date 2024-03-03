package com.poly.controller.login;


import com.poly.dto.ChangeInforDto;
import com.poly.dto.UserDetailDto;
import com.poly.entity.Users;
import com.poly.service.CartProductService;
import com.poly.service.CartService;
import com.poly.service.CustomerService;
import com.poly.service.LoginService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    final AuthenticationManager authenticationManager;



    @Autowired
    CustomerService customerService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CartService cartService;

    @Autowired
    CartProductService cartProductService;

    @Autowired
    LoginService loginService;

    @GetMapping("")
    public String login(HttpServletRequest request, Model model) {
       this.loginService.login(request,model);
        return "login/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registration", new Users());
        return "login/register";
    }

    @PostMapping("/register/add")
    public String AddCustomer(Model model,
                              @Valid @ModelAttribute("registration") Users customer,
                              BindingResult bindingResult,HttpSession httpSession)
            throws MessagingException {
        if (bindingResult.hasErrors()) {
            return "login/register";
        }
        for (Users cus : this.customerService.findAll()) {
            if (cus.getUsername().equals(customer.getUsername())) {
                model.addAttribute("errorUsername","(*)Tên đăng nhập đã tồn tại!");
                return "login/register";
            }
            if (cus.getEmail().equals(customer.getEmail())) {
                model.addAttribute("errorEmail","(*)Email đã tồn tại!");
                return "login/register";
            }
            if(cus.getPhoneNumber().equals(customer.getPhoneNumber()) && cus.getRoles() !=null){
                model.addAttribute("errorPhone", "(*)Số điện thoại đã tồn tại!");
                return "login/register";
            }
        }
        this.loginService.AddCustomer(model,customer,httpSession);
        return "redirect:/login/confirm-register";
    }

    @GetMapping("/confirm-register")
    public String comfirmRegister(HttpSession httpSession) {
        if (httpSession.getAttribute("randomNumber") == null) {
            return "redirect:/login/register";
        }
        return "login/confirm-register";
    }

    @PostMapping("/confirm-register")
    public String accuracy(HttpSession httpSession, @RequestParam("verification-code") String code,
                           RedirectAttributes redirectAttributes,Model model
    ) {
      this.loginService.comfirmRegister(httpSession,code,redirectAttributes);
        if(httpSession.getAttribute("confirmRegister") == "success"){
            return "redirect:/login";
        }else{
            model.addAttribute("errorRegister", "(*)Mã xác thực không đúng!");
            return "login/confirm-register";
        }
    }
    @GetMapping("/forgot-password")
    public String forgot() {
        return "login/forgot-password";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/changeInfo")
    public String changeInfor(@Valid @ModelAttribute("changeInfo") ChangeInforDto changeInforDto,BindingResult bindingResult,
                              @RequestParam("image") MultipartFile file,Model model,RedirectAttributes redirectAttributes
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();;
        UserDetailDto customerUserDetail = (UserDetailDto) userDetails;
        Users customer  =this.customerService.findById(customerUserDetail.getId()).get();
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorsBirthDay","(*) Ngày sinh không được để trống!");
            return "/user/index";
        }
        for (Users cus : this.customerService.findAll()) {
            if ( (!cus.getEmail().equals(customer.getEmail())) && cus.getEmail().equals(changeInforDto.getEmail())) {
                model.addAttribute("errorEmail","(*)Email đã tồn tại!");
                return "/user/index";
            }
            if( (!cus.getEmail().equals(customer.getEmail())) && cus.getPhoneNumber().equals(changeInforDto.getPhone())
                    && cus.getRoles() != null
            ){
                model.addAttribute("errorPhone", "(*)Số điện thoại đã tồn tại!");
                return "/user/index";
            }
        }
        this.loginService.changeInfo(changeInforDto,file,redirectAttributes);
        return "redirect:/login";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@ModelAttribute("email") String email,Model model,RedirectAttributes redirectAttributes){
               for(Users user : this.customerService.findAll()){
                   if(user.getEmail().equals(email)){
                       this.loginService.resetPassword(model,email,redirectAttributes);
                       return "redirect:/login";
                   }
               }
        model.addAttribute("errorReset", "(*)Email không tồn tại!");
        return "login/forgot-password";
    }

}
