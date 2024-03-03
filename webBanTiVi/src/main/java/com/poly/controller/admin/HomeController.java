package com.poly.controller.admin;

import com.poly.service.DashBoardService;
import com.poly.service.Impl.ProductServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class HomeController {
//
//    @Autowired
//    StaffServiceImpl staffService;

    @Autowired
    ProductServiceImpl productService;

    @Autowired
    DashBoardService dashBoardService;

    @GetMapping("")
    public String loadHome(HttpSession session, Model model) {
        session.setAttribute("pageView", "/admin/page/dashboard/dashboard.html");
        session.setAttribute("active", "/dashboard");
        model.addAttribute("billReturn", this.dashBoardService.getAllBillReturn());
        model.addAttribute("billProcessing", this.dashBoardService.getAllBillProcessing());
        model.addAttribute("billAll", this.dashBoardService.getAllBill());
        model.addAttribute("billDelivering", this.dashBoardService.getAllBillDelivering());
        model.addAttribute("productCount", this.dashBoardService.getProductCount());
        model.addAttribute("listDisCount", this.dashBoardService.getDisCount());
        model.addAttribute("listVoucher", this.dashBoardService.getVoucherCount());
        return "admin/layout";
    }


    @GetMapping("/statistic")
    public String loadStatistic(HttpSession session, Model model) {
        session.setAttribute("pageView", "/admin/page/statistic/statistic.html");
        session.setAttribute("active", "/statistic");
        return "admin/layout";
    }


    @GetMapping("/product/add-product")
    public String loadAddProduct(HttpSession session) {
        session.setAttribute("pageView", "/admin/page/product/add_product.html");
        session.setAttribute("active", "/product/add-product");
        return "admin/layout";
    }

    @GetMapping("/product/directSales")
    public String load(HttpSession session) {
//        session.setAttribute("pageView", "/admin/page/product/add_product.html");
//        session.setAttribute("active", "/product/add-product");
        return "admin/page/direct_sales/directSales";
    }
    @GetMapping("/attributes/list")
    public String loadField(HttpSession session) {
        session.setAttribute("pageView", "/admin/page/product/attributes.html");
        session.setAttribute("active", "/product/attributes");
        return "admin/layout";
    }
    @GetMapping("/brand/list")
    public String listBrand(HttpSession session) {
        session.setAttribute("pageView", "/admin/page/product/brand.html");
        session.setAttribute("active", "/product/brand");
        return "admin/layout";
    }
}
