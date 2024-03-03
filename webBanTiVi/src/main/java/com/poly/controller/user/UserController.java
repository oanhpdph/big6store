package com.poly.controller.user;

import com.poly.common.CheckLogin;
import com.poly.dto.ChangeInforDto;
import com.poly.dto.HistoryBillReturnDto;
import com.poly.dto.ReturnDto;
import com.poly.dto.UserDetailDto;
import com.poly.entity.Bill;
import com.poly.entity.HistoryBillProduct;
import com.poly.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    BillService billService;

    @Autowired
    BillStatusService billStatusService;

    @Autowired
    ImageReturnService imageReturnService;

    @Autowired
    BillProductService billProductService;

    @Autowired
    HistoryBillProductService historyBillProductService;

    @Autowired
    VoucherService voucherService;


    @Autowired
    CheckLogin checkLogin;


    @GetMapping("/invoice")
    public String loadInvoice(HttpSession session) {
        session.setAttribute("pageView", "/user/page/invoice/search_invoice.html");
        return "/user/index";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('USER','STAFF','ADMIN')")
    public String profile(HttpSession session, Model model) {
        session.setAttribute("pageView", "/user/page/profile/profile.html");
        model.addAttribute("changeInfo", new ChangeInforDto());
        return "/user/index";
    }

    @GetMapping("/invoice/invoice_detail/{id}")
    public String loadInvoiceDetail(HttpSession session, Model model, @PathVariable("id") Integer id) {
        Bill bill = this.billService.getOneById(id);
        List<HistoryBillReturnDto> listHistoryDto = this.historyBillProductService.findAllHistoryBillReturnByIdBill(id);
        BigDecimal totalReturn = BigDecimal.ZERO;
        int check = 0;
        if (bill.getVoucher() != null && bill.getVoucher().getMinimumValue() != null) {
            for (HistoryBillReturnDto hBillReturnDto : listHistoryDto) {
                totalReturn = totalReturn.add(hBillReturnDto.getReturnMoney());
                if (bill.getTotalPrice().subtract(totalReturn).compareTo(bill.getVoucher().getMinimumValue()) < 0 && check == 0) {
                    hBillReturnDto.setReturnMoney(hBillReturnDto.getReturnMoney().subtract(bill.getVoucherValue()));
                    check = 1;
                }
            }
        }
        model.addAttribute("bill", bill);
        model.addAttribute("listHistoryReturn", listHistoryDto);
        model.addAttribute("billProducts", bill.getBillProducts());
        session.setAttribute("pageView", "/user/page/invoice/detail_invoice.html");
        return "/user/index";
    }

    @GetMapping("/order")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','STAFF')")
    public String order(HttpSession session, Model model) {
        UserDetailDto customerUserDetail = this.checkLogin.checkLogin();
        List<Bill> billList = this.billService.findAllBillByUser(customerUserDetail.getId());
        List<Integer> idBillList = this.historyBillProductService.findIdBillByStatusAndUserReturn(1, customerUserDetail.getId());
        List<Integer> idBillALlUser = this.historyBillProductService.findIbBillByIdUser(customerUserDetail.getId());
        List<Boolean> listCheckCondtionReturn = new ArrayList<>();
        List<Boolean> listCompareBillAndHistory = new ArrayList<>();
        boolean check = false;
        int count = 0;
        for (Bill bill : billList) {
            for (Integer idBAU : idBillALlUser) {
                if (bill.getId().equals(idBAU)) {
                    count = 1;
                    for (Integer id : idBillList) {
                        if (bill.getId().equals(id)) {
                            check = true;
                        }
                    }
                }
            }
            if (count == 0) {
                check = false;
            }
            listCheckCondtionReturn.add(check);
            check = false;
            count = 0;
            Boolean compareBillAndHistory = this.historyBillProductService.compareQuantityBillAndHistoryPoduct(bill.getId());
            listCompareBillAndHistory.add(compareBillAndHistory);
        }
        List<Boolean> listcheck = this.billService.checkValidationReturn(-1);
        model.addAttribute("check", listcheck);
        model.addAttribute("listCheckConditionReturn", listCheckCondtionReturn);
        model.addAttribute("listCompareBillAndHistory", listCompareBillAndHistory);
        model.addAttribute("bill", billList);
        session.setAttribute("activeOrder", 0);
        session.setAttribute("pageView", "/user/page/profile/order.html");
        return "/user/index";
    }

    @GetMapping("/order/status/{id}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','STAFF')")
    public String orderStatus(HttpSession session, Model model, @PathVariable("id") Integer id) {
        UserDetailDto customerUserDetail = this.checkLogin.checkLogin();
        List<Bill> billList = this.billService.findBillByUserAndStatus(customerUserDetail.getId(), id);
        List<Integer> idBillList = this.historyBillProductService.findIdBillByStatusAndUserReturn(1, customerUserDetail.getId());
        List<Integer> idBillALlUser = this.historyBillProductService.findIbBillByIdUser(customerUserDetail.getId());
        List<Boolean> listCheckCondtionReturn = new ArrayList<>();
        List<Boolean> listCompareBillAndHistory = new ArrayList<>();
        boolean check = false;
        int count = 0;
        for (Bill bill : billList) {
            for (Integer idBAU : idBillALlUser) {
                if (bill.getId().equals(idBAU)) {
                    count = 1;
                    for (Integer temp : idBillList) {
                        if (bill.getId().equals(temp)) {
                            check = true;
                        }
                    }
                }
            }
            if (count == 0) {
                check = false;
            }
            listCheckCondtionReturn.add(check);
            check = false;
            count = 0;
            Boolean compareBillAndHistory = this.historyBillProductService.compareQuantityBillAndHistoryPoduct(bill.getId());
            listCompareBillAndHistory.add(compareBillAndHistory);
        }
        List<Boolean> listcheck = this.billService.checkValidationReturn(id);
        model.addAttribute("check", listcheck);
        model.addAttribute("listCheckConditionReturn", listCheckCondtionReturn);
        model.addAttribute("listCompareBillAndHistory", listCompareBillAndHistory);
        model.addAttribute("bill", billList);
        model.addAttribute("check", listcheck);
        model.addAttribute("bill", billList);
        session.setAttribute("activeOrder", id);
        session.setAttribute("pageView", "/user/page/profile/order.html");
        return "/user/index";
    }

    @GetMapping("/order/bill_return")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','STAFF')")
    public String orderBillReturn(HttpSession session, Model model) {
        UserDetailDto customerUserDetail = this.checkLogin.checkLogin();
        List<Integer> listId = this.historyBillProductService.findIbBillByIdUser(customerUserDetail.getId());
        List<List<HistoryBillReturnDto>> listNew = new ArrayList<>();
        List<String> checkStatus = new ArrayList<>();
        for (Integer id : listId) {
            List<HistoryBillReturnDto> listHistoryDto = this.historyBillProductService.findAllHistoryBillReturnByIdBill(id);
            listNew.add(listHistoryDto);
        }
        model.addAttribute("listNew", listNew);
        session.setAttribute("activeOrder", 6);
        session.setAttribute("pageView", "/user/page/return_for_returned/viewReturn.html");
        return "/user/index";
    }

    @GetMapping("/order/return_nologin/{id}")
    public String orderBillReturn2(HttpSession session, Model model, @PathVariable("id") Integer id) {
        List<List<HistoryBillReturnDto>> listNew = new ArrayList<>();
        List<HistoryBillReturnDto> listHistoryDto = this.historyBillProductService.findAllHistoryBillReturnByIdBill(id);
        listNew.add(listHistoryDto);
        model.addAttribute("listNew", listNew);
        session.setAttribute("pageView", "/user/page/return_for_returned/viewReturn.html");
        return "/user/index";
    }

    @GetMapping("/order_return/detail/{id}")
    public String viewReturnOrder(HttpSession session, Model model, @PathVariable("id") Integer id) {
        session.setAttribute("pageView", "/user/page/profile/return.html");
        Bill newBill = this.historyBillProductService.listBillWhenReturned(String.valueOf(id));
        Boolean checkQuantity = this.billService.checkQuantityBillReturn(id);
        model.addAttribute("bill", newBill);
        model.addAttribute("checkQuantity", checkQuantity);
        return "/user/index";
    }


    @PostMapping("/return/{id}")
    public ResponseEntity<?> returnProduct(@PathVariable("id") Integer id,
                                        @RequestBody List<ReturnDto> returnDto, Model model, RedirectAttributes redirectAttributes) {
        this.billService.logicBillReturn(id, returnDto);
        Bill bill = this.billService.getOneById(id);
        UserDetailDto customerUserDetail = this.checkLogin.checkLogin();
        if (customerUserDetail == null) {
            return ResponseEntity.ok("/search_order_user?search="+ bill.getCode()) ;
        }
        return ResponseEntity.ok("/order/bill_return");
    }


    @GetMapping("/search_order")
    public String getSearch(HttpSession session) {
        session.setAttribute("pageView", "/user/page/search/search_order.html");
        return "/user/index";
    }

    @GetMapping("/search_order_user")
    public String getSearchOder(@ModelAttribute("search") String search, HttpSession session, Model model) {
        Bill bill = this.billService.findBillNewReturnByCode(search.trim());
        if (bill == null) {
            model.addAttribute("errorSearch", "Xin lỗi! Đơn hàng bạn tìm không tồn tại trên hệ thống!");
            return "/user/index";
        }
        List<Integer> idBillList = this.historyBillProductService.findIdBillByStatusReturn(1);
        List<Integer> idBillALlUser = this.historyBillProductService.findAllIdBillReturn();
        boolean checkDisPlayDetail = false;
        for (Integer id : idBillALlUser) {
            if (id.equals(bill.getId())) {
                checkDisPlayDetail = true;
            }
        }
        boolean checkCondition = false;
        int count = 0;
        for (Integer idBAU : idBillALlUser) {
            if (bill.getId().equals(idBAU)) {
                count = 1;
                for (Integer id : idBillList) {
                    if (bill.getId().equals(id)) {
                        checkCondition = true;
                    }
                }
            }
        }
        if (count == 0) {
            checkCondition = false;
        }
        Boolean compareBillAndHistory = this.historyBillProductService.compareQuantityBillAndHistoryPoduct(bill.getId());
        Boolean check = this.billService.checkValidateReturnNologin(search.trim());
        Boolean checkQuantity = this.billService.checkConditionReturnNoLogin(search.trim());
        session.setAttribute("checkQuantity", checkQuantity);
        session.setAttribute("compareBillAndHistory", compareBillAndHistory);
        session.setAttribute("checkDisPlayDetail", checkDisPlayDetail);
        session.setAttribute("checkReturn", check);
        session.setAttribute("checkCondition", checkCondition);
        session.setAttribute("codeSearch", search);
        session.setAttribute("billSearch", bill);
        session.setAttribute("pageView", "/user/page/search/search_order.html");
        return "/user/index";
    }


    @GetMapping("/order/remove/{id}")
    public String removeOrder(@PathVariable("id") Integer id) {
        Bill billCancel = this.billService.getOneById(id);
        billCancel.setBillStatus(this.billStatusService.getOneBycode("CA"));
        this.billService.add(billCancel);
        if (checkLogin.checkLogin() != null) {
            UserDetailDto userDetailDto = checkLogin.checkLogin();
            List<Bill> billList = this.billService.findAllBillByUser(userDetailDto.getId());
            for (Bill bill : billList) {
                if (billCancel.equals(bill)) {
                    return "redirect:/order";
                }
            }
        }
        return "redirect:/search_order_user?search=" + billCancel.getCode();
    }

    @GetMapping("/view_order/detail_return/{id}/{returnTimes}")
    public String ViewOrderDetailReturn(@PathVariable("id") Integer id, @PathVariable("returnTimes") Integer returnTimes, HttpSession session, Model model) {
        HistoryBillReturnDto listHistoryDto = this.historyBillProductService.listHistoryBillAndReturnTimes(id, returnTimes);
        BigDecimal totalReturn = BigDecimal.ZERO;
        for(HistoryBillProduct historyBillProduct: listHistoryDto.getHistoryBillProductList()){
            totalReturn = totalReturn.add((historyBillProduct.getBillProduct().getPrice().subtract(historyBillProduct.getBillProduct().getReducedMoney())).multiply(BigDecimal.valueOf(historyBillProduct.getQuantityAcceptReturn())));
        }
        Bill bill = this.billService.getOneById(id);
        model.addAttribute("listHDTO", listHistoryDto);
        model.addAttribute("totalReturn", totalReturn);
        session.setAttribute("pageView", "/user/page/return_for_returned/detail_viewReturn.html");
        return "/user/index";
    }

}
