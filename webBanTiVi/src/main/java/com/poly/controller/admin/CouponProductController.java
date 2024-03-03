package com.poly.controller.admin;

import com.poly.entity.ProductDetail;
import com.poly.service.Impl.CouponServiceImpl;
import com.poly.service.Impl.ProductDetailImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class CouponProductController {

    @Autowired
    CouponServiceImpl couponService;
    @Autowired
    ProductDetailImpl productDetailService;
    @GetMapping("/discountproduct/{id}")
    public String CouponProduct(HttpSession session,Model model,@PathVariable("id")Integer id,
                                @RequestParam(defaultValue="") String keyword,
                                @RequestParam(defaultValue="") String keyword2) {
        System.out.println("122");
        model.addAttribute("discount",couponService.findById(id).get());
        if(keyword.isEmpty()) {
            model.addAttribute("listallproduct",productDetailService.findAll());
        }
        else {
            model.addAttribute("listallproduct",productDetailService.findBySku("%"+keyword+"%"));
        }
        if(keyword2.isEmpty()) {
            model.addAttribute("listproduct",couponService.findById(id).get().getProductDetailList());        }
        else {
            model.addAttribute("listproduct",couponService.getAllByIdAndKeyword(id,"%"+keyword2+"%"));        }

        session.setAttribute("pageView", "/admin/page/coupon/discountProduct.html");
        return "admin/layout";
    }
    @RequestMapping("/couponproduct/add")
    public String saveCouponProduct(Model model,
                                    @ModelAttribute("discountproduct") ProductDetail productDetail) {

        productDetailService.addProductDiscount(productDetail.getCoupon(),productDetail.getId());
        return "redirect:/admin/discountproduct/"+productDetail.getCoupon().getId();
    }
    @RequestMapping("couponproduct/delete/{id}")
    public String deleteCouponProduct(Model model, @PathVariable("id")Integer id) {
        int iddiscount = productDetailService.findById(id).getCoupon().getId();
        productDetailService.deleteProductDiscount(id);
        return "redirect:/admin/discountproduct/"+iddiscount;
    }
}
