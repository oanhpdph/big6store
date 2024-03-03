package com.poly.controller.admin;

import com.poly.dto.HistoryBillReturnDto;
import com.poly.entity.Bill;
import com.poly.entity.BillProduct;
import com.poly.entity.BillStatus;
import com.poly.entity.HistoryBillProduct;
import com.poly.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class BillDetailController {


    @Autowired
    private BillService billService;

    @Autowired
    private BillStatusService billStatusService;

    @Autowired
    private DeliveryNotesSevice deliveryNotesSevice;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    HistoryBillProductService historyBillProductService;

    @GetMapping("/bill/bill_detail/{billCode}")
    public String loadBillById(HttpSession session, Model model,
                               @PathVariable(name = "billCode") Integer idBill) {
        model.addAttribute("deliveryNote", deliveryNotesSevice.getByIdBill(idBill));
        deliveryNotesSevice.getByIdBill(idBill).getDeliveryFee();
        Bill bill = billService.getOneById(idBill);
        List<HistoryBillReturnDto> listHistoryDto = this.historyBillProductService.findAllHistoryBillReturnByIdBill(idBill);
        List<List<HistoryBillReturnDto>> listNew = new ArrayList<>();
        listNew.add(listHistoryDto);
        model.addAttribute("billDetail", bill);

        List<BillProduct> billProducts = bill.getBillProducts();
        BigDecimal totalPrice = billProducts.stream()
                .map(billProduct -> billProduct.getPrice().multiply(BigDecimal.valueOf(billProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal reduceMoney = billProducts.stream()
                .map(billProduct -> billProduct.getReducedMoney().multiply(BigDecimal.valueOf(billProduct.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("listNew", listNew);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentMethod", paymentMethodService.getAll());
        model.addAttribute("totalReduceMoney", reduceMoney);
        BigDecimal totalAfter = totalPrice;
        if (bill.getVoucherValue() != null) {// giảm %
            totalAfter = totalAfter.subtract(bill.getVoucherValue());
        }
        totalAfter = totalAfter.subtract(reduceMoney);
        model.addAttribute("totalAfter", totalAfter);
        List<BillStatus> billStatusList = billStatus(billStatusService.getAll(), bill.getBillStatus().getCode());
        model.addAttribute("billStatus", billStatusList);

        session.setAttribute("pageView", "/admin/page/bill/bill_detail.html");
        session.setAttribute("active", "/bill/list_bill");
        return "/admin/layout";
    }

    @GetMapping("/bill/detail_return/{id}/{returnTimes}")
    public String ViewOrderDetailReturn(@PathVariable("id") Integer id, @PathVariable("returnTimes") Integer returnTimes, HttpSession session, Model model) {
        HistoryBillReturnDto listHistoryDto = this.historyBillProductService.listHistoryBillAndReturnTimes(id, returnTimes);
        BigDecimal totalReturn = BigDecimal.ZERO;
        for(HistoryBillProduct historyBillProduct: listHistoryDto.getHistoryBillProductList()){
            totalReturn = totalReturn.add((historyBillProduct.getBillProduct().getPrice().subtract(historyBillProduct.getBillProduct().getReducedMoney())).multiply(BigDecimal.valueOf(historyBillProduct.getQuantityAcceptReturn())));
        }
        Bill bill = this.billService.getOneById(id);
        model.addAttribute("listHDTO", listHistoryDto);
        model.addAttribute("totalReturn", totalReturn);
        session.setAttribute("pageView", "/admin/page/bill/bill_detail_return.html");
        return "/admin/layout";
    }

    @PostMapping("/bill/bill_detail/update_status/{id}")
    public String updateBillStatus(@PathVariable("id") Integer id, Model model,
                                   @RequestParam(name = "status", required = false) String status,
                                   @RequestParam(name = "paymentStatus", required = false, defaultValue = "-1") Integer paymentStatus,
                                   @RequestParam(name = "paymentMethod", required = false, defaultValue = "-1") Integer paymentMethod) {
        Bill bill = new Bill();
        bill.setBillStatus(billStatusService.getOneBycode(status));
        bill.setPaymentMethod(paymentMethodService.getOne(paymentMethod));
        bill.setPaymentStatus(paymentStatus);
        billService.update(bill, id);
        return "redirect:/admin/bill/bill_detail/" + id;
    }

    private List<BillStatus> billStatus(List<BillStatus> billStatusList, String code) {
        if (code.equals("WP")) {
            billStatusList.removeIf(s -> !s.getCode().equals("WP") &&
                    !s.getCode().equals("PG") &&
                    !s.getCode().equals("DE") &&
                    !s.getCode().equals("CO") &&
                    !s.getCode().equals("CA")
            );
        }
        if (code.equals("PG")) {
            billStatusList.removeIf(s -> !s.getCode().equals("PG") &&
                    !s.getCode().equals("DE") &&
                    !s.getCode().equals("CO") &&
                    !s.getCode().equals("CA")
            );
        }
        if (code.equals("DE")) {
            billStatusList.removeIf(s -> !s.getCode().equals("DE") &&
                    !s.getCode().equals("DEE") &&
                    !s.getCode().equals("CO") &&
                    !s.getCode().equals("RN")
            );
        }
        if (code.equals("CO")) {
            billStatusList.removeIf(s -> !s.getCode().equals("CO"));
        }
        if (code.equals("RR")) {
            billStatusList.removeIf(s -> !s.getCode().equals("RR") &&
                    !s.getCode().equals("CO") &&
                    !s.getCode().equals("WR") &&
                    !s.getCode().equals("RE")
            );
        }
        if (code.equals("WR")) {
            billStatusList.removeIf(s -> !s.getCode().equals("WR") &&
                    !s.getCode().equals("RR") &&
                    !s.getCode().equals("RE")
            );
        }
        if (code.equals("DEE")) {
            billStatusList.removeIf(s -> !s.getCode().equals("DEE") &&
                    !s.getCode().equals("DE") &&
                    !s.getCode().equals("CC")
            );
        }
        if (code.equals("CA")) {
            billStatusList.removeIf(s -> !s.getCode().equals("CA")
            );
        }
        if (code.equals("RE")) {
            billStatusList.removeIf(s -> !s.getCode().equals("RE") &&
                    !s.getCode().equals("RR")
            );
        }

        return billStatusList;
    }
}
