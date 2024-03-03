package com.poly.controller.user;

import com.poly.common.SavePdf;
import com.poly.common.SendEmail;
import com.poly.dto.BillProRes;
import com.poly.entity.Bill;
import com.poly.repository.PaymentMethodRepos;
import com.poly.service.BillService;
import com.poly.service.VNPayService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

@Controller
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    BillService billService;

    @Autowired
    private PaymentMethodRepos paymentMethodRepos;

    @Autowired
    private SavePdf savePdf;

    @Autowired
    private SendEmail sendEmail;

    @GetMapping("/vnpay")
    public String home() {
        return "/user/page/product/vnpay.html";
    }

    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") BigDecimal orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request,
                             HttpServletResponse response, Model model, @ModelAttribute(value = "billProduct") BillProRes billProRes) throws IOException {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        String responseCode = request.getParameter("vnp_ResponseCode");
paymentTime=paymentTime.substring(0,4)+"/"+paymentTime.substring(4,6)+"/"+paymentTime.substring(6,8)+" "+paymentTime.substring(8,10)+":"+paymentTime.substring(10,12)+":"+paymentTime.substring(12,14);
        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", Integer.parseInt(totalPrice) / 100);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);
        model.addAttribute("vnp_ResponseCode", responseCode);
        Bill bill = billService.findByCode(orderInfo).get();
        if ("00".equals(responseCode)) {
            // Giao dịch thành công
            // Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
//                Contract contract = contractRepository.findById(Integer.parseInt(queryParams.get("contractId")))
//                        .orElseThrow(() -> new NotFoundException("Không tồn tại hợp đồng này của sinh viên"));
//                contract.setStatus(1);
//                contractRepository.save(contract);
            bill.setPaymentStatus(1);
            bill.setPaymentMethod(paymentMethodRepos.findById(2).get());
            bill = billService.update(bill, bill.getId());
            try {
                byte[] file = savePdf.generatePdf(bill);
                if (file != null) {
                    try {
                        sendEmail.sendMessageWithAttachment(file, "Hóa đơn mua hàng.pdf", bill.getDeliveryNotes().get(0).getReceivedEmail(), "Hóa đơn mua hàng Big6 Store", "Gửi anh/chị " + bill.getCustomer().getName() + "\n"
                                + "Cảm ơn anh/chị đã tin tưởng và mua hàng tại cửa hàng điện tử Big6 Store. Dưới đây cửa hàng xin gửi lại hóa đơn đặt hàng của quý khách. \n" + "Trân trọng.");
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "/user/page/product/ordersuccsess";
        }

        try {
            byte[] file = savePdf.generatePdf(bill);
            if (file != null) {
                try {
                    sendEmail.sendMessageWithAttachment(file, "Hóa đơn mua hàng.pdf", bill.getDeliveryNotes().get(0).getReceivedEmail(), "Hóa đơn mua hàng Big6 Store", "Gửi anh/chị " + bill.getCustomer().getName() + "\n"
                            + "Cảm ơn anh/chị đã tin tưởng và mua hàng tại cửa hàng điện tử Big6 Store. Dưới đây cửa hàng xin gửi lại hóa đơn đặt hàng của quý khách. \n" + "Trân trọng.");
                } catch (MessagingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "/user/page/product/orderfail";
    }

    @GetMapping("/payment-callback")
    public void paymentCallback(@RequestParam Map<String, String> queryParams, HttpServletResponse response) throws IOException {
        String vnp_ResponseCode = queryParams.get("vnp_ResponseCode");
        String contractId = queryParams.get("contractId");
        String registerServiceId = queryParams.get("registerServiceId");
        if (contractId != null && !contractId.equals("")) {
            if ("00".equals(vnp_ResponseCode)) {
                // Giao dịch thành công
                // Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
//                Contract contract = contractRepository.findById(Integer.parseInt(queryParams.get("contractId")))
//                        .orElseThrow(() -> new NotFoundException("Không tồn tại hợp đồng này của sinh viên"));
//                contract.setStatus(1);
//                contractRepository.save(contract);
                response.sendRedirect("http://localhost:4200/info-student");
            } else {
                // Giao dịch thất bại
                // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
                response.sendRedirect("http://localhost:4200/payment-failed");

            }
        }
        if (registerServiceId != null && !registerServiceId.equals("")) {
            if ("00".equals(vnp_ResponseCode)) {
                // Giao dịch thành công
                // Thực hiện các xử lý cần thiết, ví dụ: cập nhật CSDL
//                RegisterServices registerServices = registerServicesRepository.findById(Integer.parseInt(queryParams.get("registerServiceId")))
//                        .orElseThrow(() -> new NotFoundException("Không tồn tại dịch vụ này của sinh viên"));
//                registerServices.setStatus(1);
//                registerServicesRepository.save(registerServices);
                response.sendRedirect("http://localhost:8080/info-student");
            } else {
                // Giao dịch thất bại
                // Thực hiện các xử lý cần thiết, ví dụ: không cập nhật CSDL\
                response.sendRedirect("http://localhost:8080/payment-failed");

            }
        }
    }
}