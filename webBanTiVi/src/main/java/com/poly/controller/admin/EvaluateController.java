package com.poly.controller.admin;

import com.poly.dto.EvaluateRes;
import com.poly.entity.Evaluate;
import com.poly.service.CustomerService;
import com.poly.service.EvaluateService;
import com.poly.service.ProductDetailService;
import com.poly.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class EvaluateController {
    @Autowired
    EvaluateService evaluateService;

    @Autowired
    ProductDetailService productDetailService;

    @Autowired
    CustomerService customerService;

    @Autowired
    ProductService productService;

    @GetMapping("/evaluate/{id}")
    public String index(@ModelAttribute(name = "evaluate") EvaluateRes evaluateDto, @PathVariable Integer id, Model model, HttpSession httpSession) {
        evaluateDto.setProduct(id);
        Page<Evaluate> evaluates = evaluateService.getAll(evaluateDto);
        model.addAttribute("list", evaluates);
        httpSession.setAttribute("pageView", "/admin/page/evaluate/evaluate.html");
        httpSession.setAttribute("active", "/evaluate/list");
        model.addAttribute("product", productService.findById(id));
        return "admin/layout";
    }

    @PostMapping("/evaluate/update")
    public ResponseEntity<?> updateAccount(@RequestBody EvaluateRes evaluate) {
        return ResponseEntity.ok(evaluateService.update(evaluate));
    }
}
