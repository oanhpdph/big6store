package com.poly.controller.user;

import com.poly.common.CheckLogin;
import com.poly.dto.ProductDetailDto;
import com.poly.dto.UserDetailDto;
import com.poly.entity.Brand;
import com.poly.entity.Cart;
import com.poly.entity.Product;
import com.poly.entity.Users;
import com.poly.service.BrandService;
import com.poly.service.CartService;
import com.poly.service.CustomerService;
import com.poly.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeUserController {
    @Autowired
    CartService cartService;

    @Autowired
    private CheckLogin checkLogin;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @GetMapping("/")
    public String loadHome(HttpSession session, Model model) {
        session.setAttribute("pageView", "/user/page/home/home.html");
        model.addAttribute("active", "home");
        ProductDetailDto productDetailDto = new ProductDetailDto();
        productDetailDto.setSort(1);
        productDetailDto.setSize(20);
        productDetailDto.setActive(true);

        productDetailDto.setGroup(1);

        Page<Product> page = productService.findAll(productDetailDto);
        model.addAttribute("listTivi", productService.getProduct(new ArrayList<>(), page.getContent()));

        productDetailDto.setGroup(2);
        Page<Product> phuKien = productService.findAll(productDetailDto);
        model.addAttribute("listPhuKien", productService.getProduct(new ArrayList<>(), phuKien.getContent()));

        productDetailDto.setSize(10);

        productDetailDto.setGroup(0);
        Page<Product> productNew = productService.findAll(productDetailDto);
        model.addAttribute("listNewProduct", productService.getProduct(new ArrayList<>(), productNew.getContent()));

        productDetailDto.setSort(3);
        Page<Product> topRate = productService.findAll(productDetailDto);
        model.addAttribute("listTopRate", productService.getProduct(new ArrayList<>(), topRate.getContent()));


        UserDetailDto userDetailDto = checkLogin.checkLogin();
        if (userDetailDto != null) {
            Cart cart = cartService.getOneByUser(userDetailDto.getId());
            if (cart == null) {
                cart = new Cart();
                Optional<Users> optional = customerService.findById(userDetailDto.getId());
                if (optional.isPresent()) {
                    cart.setCustomer(optional.get());
                    cartService.save(cart);
                }
            }
            session.setAttribute("list", cart.getListCartPro());
            session.setAttribute("user", userDetailDto);
        } else {
            session.setAttribute("list", cartService.getitems());
            session.setAttribute("user", null);
        }
        return "/user/index";
    }

    @GetMapping("/tivi")
    public String loadProduct(HttpSession session, Model model) {
        List<Brand> list = brandService.findAll();
        model.addAttribute("listBrand", list.stream().filter(brand -> brand.isActive() == true).toList());

        session.setAttribute("pageView", "/user/page/product/tivi.html");
        model.addAttribute("active", "tivi");
        return "/user/index";
    }

    @GetMapping("/tivi/get")
    public ResponseEntity<?> loadProductTivi(@ModelAttribute ProductDetailDto productDetailDto) {
//        productDetailDto.setSize(1);
        productDetailDto.setActive(true);
        productDetailDto.setGroup(1);

        Page<Product> page = productService.findAll(productDetailDto);
        return ResponseEntity.ok(productService.getProduct(new ArrayList<>(), page.getContent()));
    }

    @GetMapping("/accessory")
    public String loadAccessory(HttpSession session, Model model) {
        List<Brand> list = brandService.findAll();
        model.addAttribute("listBrand", list.stream().filter(brand -> brand.isActive() == true).toList());

        session.setAttribute("pageView", "/user/page/product/accessory.html");
        model.addAttribute("active", "accesory");
        return "/user/index";
    }

    @GetMapping("/accessory/get")
    public ResponseEntity<?> apiLoadAccessory(@ModelAttribute ProductDetailDto productDetailDto) {
        productDetailDto.setActive(true);
        productDetailDto.setGroup(2);

        Page<Product> page = productService.findAll(productDetailDto);
        return ResponseEntity.ok(productService.getProduct(new ArrayList<>(), page.getContent()));
    }


    @GetMapping("/policy")
    public String policy(HttpSession session, Model model) {
        session.setAttribute("pageView", "/user/page/policy/policy.html");
        model.addAttribute("active", "policy");

        return "user/index";
    }
}
