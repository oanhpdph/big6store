package com.poly.controller.admin;

import com.poly.common.UploadFile;
import com.poly.entity.Coupon;
import com.poly.service.CouponService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
@RequestMapping("/admin")
public class CouponController {
    @Autowired
    CouponService couponService;

    @GetMapping("/coupon/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(couponService.findAll().stream().map(Coupon::getCode).collect(Collectors.toList()));
    }

    @GetMapping("/coupon/list")
    public String loadCoupon(HttpSession session, Model model) {
        session.setAttribute("pageView", "/admin/page/coupon/coupon.html");
        session.setAttribute("active", "/promotion/coupon");
        model.addAttribute("coupon", new Coupon());
        model.addAttribute("listCou", this.couponService.findAll());
        return "admin/layout";
    }

    @PostMapping("/coupon/save")
    public String addCoupon(@Valid @ModelAttribute("coupon") Coupon coupon, BindingResult result, Model model, @RequestParam("avatar") MultipartFile file) {
        if (result.hasErrors()) {
            return "admin/layout";
        }

        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // xóa ký tự đặc biệt
        coupon.setImage(fileName);
        this.couponService.save(coupon);
        String uploadDir = "src/main/resources/static/image";
        try {
            UploadFile.saveFile(uploadDir, fileName, file);
        } catch (IOException e) {
            //
            e.printStackTrace();
        }
        model.addAttribute("listCou", couponService.findAll());
        return "redirect:/admin/coupon/list";
    }

    @GetMapping("/coupon/edit/{id}")
    public String showUpdateCoupon(Model model, @PathVariable("id") Integer id) {
        Coupon cou = couponService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("coupon", cou);
        model.addAttribute("listCou", couponService.findAll());
        return "admin/layout";
    }

    @PostMapping("/coupon/update")
    public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon) {
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Coupon findCoupon = this.couponService.findById(coupon.getId()).orElse(null);

//        findCoupon.setCode(coupon.getCode());
//        findCoupon.setValue(coupon.getValue());
        findCoupon.setActive(coupon.isActive());
//        findCoupon.setDateStart(coupon.getDateStart());
//        findCoupon.setDateEnd(coupon.getDateEnd());
//        System.out.println(coupon.getImage());
//        if (!"".equals(fileName)) {
//            findCoupon.setImage(fileName);
//            String uploadDir = "src/main/resources/static/image";
//            try {
//                UploadFile.saveFile(uploadDir, fileName, file);
//            } catch (IOException e) {
//                //
//                e.printStackTrace();
//            }
//        }
        couponService.save(findCoupon);
        return ResponseEntity.ok(200);
    }

    @GetMapping("/coupon/delete/{id}")
    public String deleteCoupon(Model model, @PathVariable("id") Integer id) {
        Coupon coupon = couponService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        couponService.deleteById(id);
        return "redirect:/admin/coupon/list";
    }
}