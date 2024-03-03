package com.poly.controller.admin;

import com.poly.entity.PaymentMethod;
import com.poly.service.PaymentMethodService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodService paymentMethodService;

    @GetMapping("/bill/payment_method")
    public String loadPaymentMethod(HttpSession session, Model model) {
        model.addAttribute("listPaymentMethod", paymentMethodService.getAll());
        model.addAttribute("paymentMethodAtt", new PaymentMethod());
        session.setAttribute("pageView", "/admin/page/bill/payment_method.html");
        session.setAttribute("active", "/bill/payment_method");
        return "admin/layout";
    }

    @PostMapping("/bill/payment_method")
    public String addPaymentMethod(@Valid @ModelAttribute("paymentMethodAtt") PaymentMethod paymentMethod, BindingResult result, Model model) {
        paymentMethodService.add(paymentMethod);
        model.addAttribute("paymentMethodAtt", new PaymentMethod());
        return "redirect:/admin/bill/payment_method";
    }

    @GetMapping("/bill/payment_method/delete/{id}")
    public String deletePaymentMethod(@PathVariable("id") Integer id) {
        paymentMethodService.delete(id);
        return "redirect:/admin/bill/payment_method";
    }

    @PostMapping("/bill/payment_method/update/{id}")
    public String updatePaymentMethod(@PathVariable("id") Integer id, @ModelAttribute("paymentMethodAtt") PaymentMethod paymentMethod) {
        paymentMethodService.update(paymentMethod,id);
        return "redirect:/admin/bill/payment_method";
    }

}
