package com.poly.controller.admin;

import com.poly.dto.ProductDetailDto;
import com.poly.dto.ProductDetailListDto;
import com.poly.entity.Product;
import com.poly.entity.ProductDetail;
import com.poly.entity.ProductDetailField;
import com.poly.service.ProductDetailService;
import com.poly.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    ProductDetailService productDetailService;

    @GetMapping("/product/list")
    public String getAll(@ModelAttribute(name = "productDTO") ProductDetailDto detailDto, HttpSession session, Model model) {
        session.setAttribute("pageView", "/admin/page/product/list_product.html");
        session.setAttribute("active", "/product/list-product");
        model.addAttribute("listProduct", productService.findAll(detailDto));
        model.addAttribute("countProduct", productService.findAll());

        model.addAttribute("countDetail", productDetailService.findAll());
        return "/admin/layout";
    }

    @GetMapping("/product/list_detail")
    public String getAllListDetail(@ModelAttribute(name = "detailDTO") ProductDetailListDto detailDto, HttpSession session, Model model) {
        session.setAttribute("pageView", "/admin/page/product/list_product_detail.html");
        session.setAttribute("active", "/product/list-product");
        model.addAttribute("countProduct", productService.findAll());

        Page<ProductDetail> page = productDetailService.findAll(detailDto);
        List<String> list = new ArrayList<>();
        for (ProductDetail productDetail : page.getContent()) {
            String name = productDetail.getProduct().getNameProduct();
            List<String> temp = new ArrayList<>();
            temp.add(name);
            temp.add("[");
            for (ProductDetailField string : productDetail.getFieldList()) {
                temp.add(string.getValue());
                temp.add("-");
            }
            temp.remove(temp.size() - 1);
            temp.add("]");
            list.add(String.join(" ", temp));
        }
        model.addAttribute("listNameProduct", list);
        model.addAttribute("listProductDetail", page);
        model.addAttribute("countDetail", productDetailService.findAll());

        return "/admin/layout";
    }

    @GetMapping("/product/sku")
    public ResponseEntity<?> getALl() {
        return ResponseEntity.ok(productService.findAll().stream().map(Product::getSku).collect(Collectors.toList()));
    }

}
