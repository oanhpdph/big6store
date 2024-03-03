package com.poly.controller.admin;

import com.poly.dto.formReturnDto;
import com.poly.entity.Bill;
import com.poly.entity.BillProduct;
import com.poly.entity.HistoryBillProduct;
import com.poly.service.BillProductService;
import com.poly.service.BillService;
import com.poly.service.BillStatusService;
import com.poly.service.HistoryBillProductService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class BillReturnController {

      Date date =new Date();

    @Autowired
    BillService billService;

    @Autowired
    BillProductService billProductService;

    @Autowired
    BillStatusService billStatusService;

    @Autowired
    HistoryBillProductService historyBillProductService;

    @GetMapping("/bill/list_invoice_return")
    public String getListInvoice(HttpSession session, Model model){
        session.setAttribute("pageView", "/admin/page/bill/invoice_return.html");
        session.setAttribute("active", "/bill/invoice_return");
        List<Integer> listId = this.historyBillProductService.findIbBillByStatusReturn(1);
        List<Bill> ListB = new ArrayList<>();
        for(Integer id :  listId) {
            Bill bill = this.billService.getOneById(id);
            ListB.add(bill);
        }
        Collections.reverse(ListB);
        model.addAttribute("listBill", ListB);
        model.addAttribute("formReturnDto", new formReturnDto());
        return "admin/layout";
    }
    @GetMapping("/invoice_return/{id}")
    public String getInvoiceReturnBill(HttpSession session, Model model, @PathVariable("id") Integer id,
                                   @ModelAttribute("formReturnDto") formReturnDto dto){
        List<HistoryBillProduct> product=this.historyBillProductService.findHistoryBillProductReturn(1,id);
        session.setAttribute("pageView", "/admin/page/bill/billProduct_return.html");
        session.setAttribute("active", "/bill/invoice_return");
        model.addAttribute("listBillReturn",product);
        session.setAttribute("idBillReturn", id);
        return "admin/layout";
    }
    @GetMapping("/invoice_return/agree/{id}")
    public String getInvoiceReturn(HttpSession session, Model model, @PathVariable("id") Integer id,
                                   @ModelAttribute("formReturnDto") formReturnDto dto){
        session.setAttribute("pageView", "/admin/page/bill/billProduct_return.html");
        session.setAttribute("active", "/bill/invoice_return");
        List<HistoryBillProduct> product = this.historyBillProductService.findHistoryBillProductReturn(1,id);
        if(product.size()==0){
            for(HistoryBillProduct historyBillProduct  : product){
                historyBillProduct.setStatus(2);
            }
            return "redirect:/admin/bill/list_invoice_return";
        }
        model.addAttribute("listBillReturn",product);
        return "admin/layout";
    }
    @GetMapping("/invoice_return/refuse/{id}")
    public String getRefuseReturn(HttpSession session, Model model, @PathVariable("id") Integer id,
                                   @ModelAttribute("formReturnDto") formReturnDto dto){
        List<HistoryBillProduct> product = this.historyBillProductService.findHistoryBillProductReturn(1,id);
        if(product.size()==0){
            for(HistoryBillProduct historyBillProduct  : product){
                historyBillProduct.setStatus(2);
            }
            return "redirect:/admin/bill/list_invoice_return";
        }
        model.addAttribute("listBillReturn",product);
        session.setAttribute("pageView", "/admin/page/bill/billProduct_return.html");
        session.setAttribute("active", "/bill/invoice_return");
        return "admin/layout";
    }

    @PostMapping("/refuse/{id}")
    public String getRefuse(HttpSession session,
                            @PathVariable("id") Integer id,
                            @Valid @ModelAttribute("formReturnDto") formReturnDto dto,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes
                            ){
        BillProduct billProduct =this.billProductService.edit(id);
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errorQuantityNull","Vui lòng điền thông tin!");
            redirectAttributes.addFlashAttribute("errorNoteNull","Vui lòng điền thông tin!");
            return "redirect:/admin/invoice_return/"+billProduct.getBill().getId();
        }
        if(Integer.parseInt(dto.getQuantity()) > billProduct.getQuantity()){
            redirectAttributes.addFlashAttribute("errorAdminQuantity","(*)Số lượng sản phẩm được trả không lớn hơn số lương yêu cầu!");
            return "redirect:/admin/invoice_return/refuse/"+billProduct.getBill().getId();
        }
        if(Integer.parseInt(dto.getQuantity()) <0 ){
            redirectAttributes.addFlashAttribute("errorAdminQuantity","(*)Số lượng sản phẩm trả không nhỏ nhỏ hơn 0!");
            return "redirect:/admin/invoice_return/refuse/"+billProduct.getBill().getId();
        }
        Integer idBill =Integer.parseInt(session.getAttribute("idBillReturn").toString());
        HistoryBillProduct historyBillProduct = this.historyBillProductService.findByBillProductAndReturnTimes(id,idBill);
        historyBillProduct.setNote(dto.getNote());
        historyBillProduct.setDate(date);
        historyBillProduct.setStatusBillProduct(2);
        historyBillProduct.setStatus(0);
        historyBillProduct.setQuantityAcceptReturn(historyBillProduct.getQuantityRequestReturn()-Integer.parseInt(dto.getQuantity()));
        historyBillProduct.setBill(this.billService.getOneById(idBill));
        historyBillProduct.setBillProduct(this.billProductService.edit(id));
        this.historyBillProductService.save(historyBillProduct);
        return "redirect:/admin/invoice_return/refuse/"+billProduct.getBill().getId();
    }

    @RequestMapping("/agree/{id}")
    public String getAgree(@PathVariable("id") Integer id,
                           @Valid @ModelAttribute("formReturnDto") formReturnDto dto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,HttpSession session
    ){
        BillProduct billProduct =this.billProductService.edit(id);
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errorQuantityNull","Vui lòng điền thông tin!");
            redirectAttributes.addFlashAttribute("errorNoteNull","Vui lòng điền thông tin!");
            return "redirect:/admin/invoice_return/"+billProduct.getBill().getId();
        }
        if(Integer.parseInt(dto.getQuantity()) > billProduct.getQuantity()){
            redirectAttributes.addFlashAttribute("errorAdminQuantity","(*)Số lượng sản phẩm được trả không lớn hơn số lương yêu cầu!");
            return "redirect:/admin/invoice_return/agree/"+billProduct.getBill().getId();
        }
        if(Integer.parseInt(dto.getQuantity()) <0 ){
            redirectAttributes.addFlashAttribute("errorAdminQuantity","(*)Số lượng sản phẩm trả không nhỏ nhỏ hơn 0!");
            return "redirect:/admin/invoice_return/agree/"+billProduct.getBill().getId();
        }
        Integer idBill =Integer.parseInt(session.getAttribute("idBillReturn").toString());
        HistoryBillProduct historyBillProduct = this.historyBillProductService.findByBillProductAndReturnTimes(id, idBill);
        historyBillProduct.setQuantityAcceptReturn(Integer.parseInt(dto.getQuantity()));
        historyBillProduct.setNote(dto.getNote());
        historyBillProduct.setStatusBillProduct(3);
        historyBillProduct.setStatus(0);
        historyBillProduct.setDate(date);
        historyBillProduct.setBill(this.billService.getOneById(idBill));
        historyBillProduct.setBillProduct(this.billProductService.edit(id));
        this.historyBillProductService.save(historyBillProduct);
        return "redirect:/admin/invoice_return/agree/"+billProduct.getBill().getId();
    }
    @GetMapping("/agree")
    public String getViewAgree(HttpSession session,Model model){
        session.setAttribute("pageView", "/admin/page/bill/agree_bill_return.html");
        List<HistoryBillProduct> listHistoryBillProducts = this.historyBillProductService.findAllByStatus(3);
        Collections.reverse(listHistoryBillProducts);
        model.addAttribute("listAgree",listHistoryBillProducts);
        return "admin/layout";
    }
    @GetMapping("/refuse")
    public String getViewRefuse(HttpSession session,Model model){
        session.setAttribute("pageView", "/admin/page/bill/refuse_bill_return.html");
        List<HistoryBillProduct> listHistoryBillProducts = this.historyBillProductService.findAllByStatus(2);
        Collections.reverse(listHistoryBillProducts);
        model.addAttribute("listRefuse",listHistoryBillProducts);
        return "admin/layout";
    }

}
