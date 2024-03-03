package com.poly.service.Impl;

import com.poly.common.RandomNumber;
import com.poly.common.SendEmail;
import com.poly.dto.CartDto;
import com.poly.dto.ChangeInforDto;
import com.poly.dto.LoginDto;
import com.poly.dto.UserDetailDto;
import com.poly.entity.Cart;
import com.poly.entity.CartProduct;
import com.poly.entity.Users;
import com.poly.service.CartProductService;
import com.poly.service.CartService;
import com.poly.service.CustomerService;
import com.poly.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private SendEmail sendEmail;

    @Autowired
    CustomerService customerService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CartService cartService;

    @Autowired
    CartProductService cartProductService;

    @Override
    public void login(HttpServletRequest request, Model model) {
        String error = (String) request.getSession().getAttribute("error");
        if (error != null) {
            model.addAttribute("error", "error");
            request.getSession().setAttribute("error", null);
        }
        model.addAttribute("login", new LoginDto());
    }

    @Override
    public void AddCustomer(Model model, Users customer, HttpSession httpSession) {
        RandomNumber rand = new RandomNumber();
        int value = rand.randomNumber();
        httpSession.setAttribute("accountRegis", customer);
        httpSession.setAttribute("randomNumber", value);
        sendEmail.sendSimpleMessage(customer.getEmail(), "Tạo tài khoản thành viên mới trên Big6Store.vn",
                "Xin chào Bạn!,\n" +
                        "Bạn đã đăng ký tài khoản trên trang Big6Store.vn của chúng tôi.\n" +
                        "Tên đăng nhập của bạn là " + customer.getUsername() + "\n" +
                        "Hãy nhập mã dưới đây để thực hiện hoàn tất viêc đăng ký tài khoản của bạn.Lưu ý không \n" +
                        "chia sẻ mã xác thực cho bên thứ ba .Điều đó có thể dẫn tới thông tin tài khoản của bạn bị lộ.Xin cảm ơn!" + "\n" +
                        "Mã xác thực : " + value
        );
    }

    @Override
    public void comfirmRegister(HttpSession httpSession, String code, RedirectAttributes redirectAttributes) {
        Users account = (Users) httpSession.getAttribute("accountRegis");
        account.setRoles("USER");
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setAvatar("default.jpg");
        int value = (int) httpSession.getAttribute("randomNumber");
        if (code.equals(String.valueOf(value))) {
            Users u = customerService.save(account);
            if (httpSession.getAttribute("list") != null) {
                List<CartProduct> listCart = (List<CartProduct>) httpSession.getAttribute("list");
                Cart cart = new Cart();
                cart.setCustomer(account);
                this.cartService.save(cart);

                for (CartProduct cartProduct : listCart) {
                    CartDto cartDto = new CartDto();
                    cartDto.setIdProduct(cartProduct.getProduct().getId());
                    cartDto.setIdUser(u.getId());
                    cartDto.setQuantity(cartProduct.getQuantity());
                    this.cartProductService.save(cartDto);
                }
            }
            httpSession.removeAttribute("accountRegis");
            httpSession.removeAttribute("randomNumber");
            httpSession.removeAttribute("list");
            redirectAttributes.addFlashAttribute("success", "success");
            httpSession.setAttribute("confirmRegister", "success");
        }
    }

    @Override
    public void changeInfo(ChangeInforDto changeInforDto, MultipartFile file,RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();;
        UserDetailDto customerUserDetail = (UserDetailDto) userDetails;
        Users customer  =this.customerService.findById(customerUserDetail.getId()).get();
        customer.setUsername(changeInforDto.getName());
        customer.setPhoneNumber(changeInforDto.getPhone());
        customer.setBirthday(changeInforDto.getBirthday());
        customer.setGender(changeInforDto.isGender());
        if (!changeInforDto.getPassword().equals("")){
            customer.setPassword(passwordEncoder.encode(changeInforDto.getPassword()));
        }else{
            customer.setPassword(customer.getPassword());
        }
        customer.setRoles(customerUserDetail.getRoles());
        customer.setEmail(changeInforDto.getEmail());
        customer.setAddress(changeInforDto.getAddress());
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if(!"".equals(fileName)){
            customer.setAvatar(fileName);
        }
        this.customerService.save(customer);
        redirectAttributes.addFlashAttribute("updateSuccess","updateSuccess");
    }

    @Override
    public void resetPassword(Model model, String email, RedirectAttributes redirectAttributes) {
        RandomNumber rand = new RandomNumber();
        int value = rand.randomNumber();
       for(Users user : this.customerService.findAll()){
           if(user.getEmail().equals(email)){
               user.setPassword(passwordEncoder.encode(String.valueOf(value)));
               this.customerService.save(user);
               break;
           }
       }

        redirectAttributes.addFlashAttribute("resetPass","resetPass");
        sendEmail.sendSimpleMessage(email, "Lấy lại mật khẩu trên Big6Store.vn",
                "Xin chào Bạn!,\n" +
                        "Mật khẩu mới của bạn là : " + value
        );
    }
}
