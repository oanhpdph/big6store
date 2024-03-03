package com.poly.controller.admin;

import com.poly.dto.VoucherCustomerRes;
import com.poly.service.CustomerService;
import com.poly.service.Impl.VoucherCustomerServiceImpl;
import com.poly.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class VoucherCustomerController {
    @Autowired
    CustomerService customerService;


    @Autowired
    VoucherService voucherService;
    @Autowired
    VoucherCustomerServiceImpl voucherCustomerService;


    // voucherCustomer
    @GetMapping("/vouchercustomer/{id}")
    public String voucherCustomer(HttpSession session, Model model,
                                  @PathVariable("id")Integer id,@RequestParam(defaultValue="") String keyword) {
        model.addAttribute("voucher",voucherService.findById(id).get());
        session.setAttribute("active", "/voucherCustomer");
        session.setAttribute("listCustomer", this.customerService.findAll());
        session.setAttribute("listVoucher", this.voucherService.findAllList());
        model.addAttribute("voucherCustomer", new VoucherCustomerRes());
        if(keyword.isEmpty()) {
            model.addAttribute("listVoucherCustomer", voucherCustomerService.findAllByVoucher(id));
        }
        else {
            model.addAttribute("listVoucherCustomer", voucherCustomerService.findAllByKeyword("%"+keyword+"%", id));
        }
        session.setAttribute("pageView", "/admin/page/voucher/voucherCustomer.html");
        return "admin/layout";
    }


    @RequestMapping("/vouchercustomer/update/{id}")
    public String updatevoucherCustomer(HttpSession session,@PathVariable("id") Integer id, @Valid @ModelAttribute("voucherCustomer")  VoucherCustomerRes voucherCustomerRes, BindingResult result, Model model) {
        this.voucherCustomerService.updateById(voucherCustomerRes,id);
        return "redirect:/admin/vouchercustomer/"+voucherCustomerService.findById(id).get().getVoucher().getId();
    }
    @GetMapping("/vouchercustomer/delete/{id}")
    public String deleteVoucherCustomer(HttpSession session,@PathVariable("id") Integer id, @Valid @ModelAttribute("voucherCustomer")  VoucherCustomerRes voucherCustomerRes, BindingResult result, Model model) {
        int idvoucher = voucherCustomerService.findById(id).get().getVoucher().getId();
        this.voucherCustomerService.delete(id);
        return "redirect:/admin/vouchercustomer/"+idvoucher;
    }

}
