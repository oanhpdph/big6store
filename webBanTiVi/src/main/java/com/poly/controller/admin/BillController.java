package com.poly.controller.admin;

import com.poly.common.ExportExcel;
import com.poly.common.SavePdf;
import com.poly.dto.SearchBillDto;
import com.poly.entity.Bill;
import com.poly.entity.BillProduct;
import com.poly.service.BillProductService;
import com.poly.service.BillService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class BillController {

    @Autowired
    private BillProductService billProductService;

    @Autowired
    private BillService billService;

    @Autowired
    private ExportExcel exportExcel;

    @Getter
    @Setter
    public class DataExportExcel {
        private int stt;
        private String code;
        private String name;
        private String phoneNumber;
        private String product;
        private String dateCreate;
        private Long totalPrice;
        private Long reduce;
        private Long voucher;
        private String billStatus;
        private String paymentStatus;
        private String note;
    }

    Page<Bill> bills;

    @GetMapping("/bill_product")
    public String index(Model model) {
        model.addAttribute("bill_product", billProductService.getAll());
        return "";
    }

    @PostMapping("/bil_product/add")
    public String add(BillProduct product) {
        billProductService.save(product);
        return "redirect:/admin/bill_product";
    }

    @GetMapping("/bil_product/delete/{id}")
    public String delete(Integer id) {
        billProductService.delete(id);
        return "redirect:/admin/bill_product";
    }

    @GetMapping("/bil_product/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        BillProduct product = billProductService.edit(id);
        model.addAttribute("bill_product", product);
        return "redirect:/admin/bill_product";
    }

    @PostMapping("/bil_product/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @ModelAttribute("billproduct") BillProduct billproduct) {
        billProductService.save(billproduct);
        return "redirect:/admin/bill_product";
    }

    @DeleteMapping("/bill/delete/{billCode}")
    public String deleteBill(HttpSession session, @PathVariable(name = "billCode") Integer idBill, Model model) {
        Boolean check = billService.delete(idBill);
        session.setAttribute("pageView", "/admin/page/bill/bill.html");
        session.setAttribute("active", "/bill/list_bill");
        return "/admin/layout";
    }


    @GetMapping(value = {"/bill/list_bill"})
    public String loadBill(HttpSession session, Model model,
                           @RequestParam(name = "page", required = false, defaultValue = "1") Integer pageRequest,
                           @RequestParam(name = "size", required = false, defaultValue = "10") Integer sizeRequest,
                           @ModelAttribute(name = "search") SearchBillDto search) {
        //set trang
        session.setAttribute("pageView", "/admin/page/bill/bill.html");
        session.setAttribute("active", "/bill/list_bill");
        // tìm kiếm
        if (pageRequest <= 0) {
            pageRequest = 1;
        }
        if (sizeRequest < 1) {
            sizeRequest = 10;
        }
        session.setAttribute("size", sizeRequest);
        session.setAttribute("page", pageRequest);

        if (search.getBillStatus().isEmpty()) {
            search.setBillStatus("doncho");
        }
        Pageable pageable = PageRequest.of(pageRequest - 1, sizeRequest);
        Page<Bill> pagebill = billService.loadData(search, pageable);
        bills = billService.loadData(search, PageRequest.of(pageRequest - 1, (int) pagebill.getTotalElements() < 1 ? 1 : (int) pagebill.getTotalElements()));
        List<Bill> bill1 = billService.all();
        model.addAttribute("cho", bill1.stream().filter(bill -> bill.getBillStatus().getCode().equals("WP")).toList());
        model.addAttribute("chuanbi", bill1.stream().filter(bill -> bill.getBillStatus().getCode().equals("PG")).toList());
        model.addAttribute("danggiao", bill1.stream().filter(bill -> bill.getBillStatus().getCode().equals("DE")).toList());
        model.addAttribute("hoanthanh", bill1.stream().filter(bill -> bill.getBillStatus().getCode().equals("CO")).toList());
        model.addAttribute("hoantra", bill1.stream().filter(bill -> bill.getBillStatus().getCode().equals("RN")).toList());
        model.addAttribute("huy", bill1.stream().filter(bill -> bill.getBillStatus().getCode().equals("CA")).toList());
        model.addAttribute("trahang", bill1.stream().filter(bill -> bill.getBillStatus().getCode().equals("RR") || bill.getBillStatus().getCode().equals("WR") || bill.getBillStatus().getCode().equals("RE")).toList());
        model.addAttribute("listBill", pagebill);
//        pagebill.getContent().get(0).getDeliveryNotes().get(0).getDeliveryFee();
        model.addAttribute("totalElements", pagebill.getTotalElements());
        return "/admin/layout";
    }

    @GetMapping("/bill/list_bill/download_excel")
    public void exportIntoExcelFile(HttpServletResponse response, HttpSession session, Model model) {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());

//        bills = (Page<Bill>) model.getAttribute("listBill");

        List<String> header = listHeader();

        List<DataExportExcel> dataExport = new ArrayList<>();
        listData(bills, dataExport);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Danh_sach_hoa_don_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        exportExcel.exportExel(response, dataExport, header);
    }

    @GetMapping("/bill/list_bill/downloadpdf")
    public void exportPdf(HttpServletResponse response, HttpSession session) throws IOException {
        List<String> header = listHeader();

        List<DataExportExcel> dataExport = new ArrayList<>();
        listData(bills, dataExport);

        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Danh_sach_hoa_don_" + currentDateTime + ".pdf";

        response.setHeader(headerKey, headerValue);
        SavePdf savePdf = new SavePdf();
        savePdf.savePdf(response, dataExport, header);
    }

    private void listData(Page<Bill> bills, List<DataExportExcel> dataExport) {
        int index = 1;
        for (Bill b : bills.getContent()) {
            DataExportExcel dataExportExcel = new DataExportExcel();
            dataExportExcel.setStt(index);
            dataExportExcel.setCode(b.getCode());
            dataExportExcel.setName(b.getCustomer().getName());
            dataExportExcel.setPhoneNumber(b.getCustomer().getPhoneNumber());
            dataExportExcel.setDateCreate(String.valueOf(b.getCreateDate()));

            BigDecimal totalPrice = b.getBillProducts().stream()
                    .map(billProduct -> billProduct.getPrice().multiply(BigDecimal.valueOf(billProduct.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal reduceMoney = b.getBillProducts().stream()
                    .map(billProduct -> billProduct.getReducedMoney().multiply(BigDecimal.valueOf(billProduct.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalAfter = new BigDecimal('0');
            if (b.getVoucher() == null) {
                totalAfter = totalPrice.subtract(reduceMoney);
            }
            dataExportExcel.setTotalPrice(totalAfter.longValue());

            dataExportExcel.setVoucher(b.getVoucherValue() != null ? b.getVoucherValue().longValue() : 0);
            dataExportExcel.setReduce(reduceMoney.longValue());
            dataExportExcel.setProduct(String.join(",", b.getBillProducts().stream().map(billProduct -> billProduct.getProduct().getSku()).collect(Collectors.toList())));
            dataExportExcel.setBillStatus(b.getBillStatus().getStatus());
            dataExportExcel.setPaymentStatus(b.getPaymentStatus() == 1 ? "Đã thanh toán" : b.getPaymentStatus() == 2 ? "Chưa thanh toán" : "Đã hoàn tiền");
            dataExportExcel.setNote(b.getNote());
            dataExport.add(dataExportExcel);
            index++;
        }
    }

    private List<String> listHeader() {
        List<String> header = new ArrayList<>();
        header.add("Stt");
        header.add("Mã hóa đơn");
        header.add("Tên khách hàng");
        header.add("Số điện thoại");
        header.add("Sản phẩm");
        header.add("Thời gian tạo");
        header.add("Tổng tiền");
        header.add("Giảm giá");
        header.add("Voucher");
        header.add("Trạng thái hóa đơn");
        header.add("Trạng thái thanh toán");
        header.add("Ghi chú");
        return header;
    }
}
