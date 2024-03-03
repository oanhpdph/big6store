package com.poly.controller.user;

import com.poly.common.CheckLogin;
import com.poly.common.SavePdf;
import com.poly.common.SendEmail;
import com.poly.dto.BillProRes;
import com.poly.dto.UserDetailDto;
import com.poly.entity.*;
import com.poly.entity.idClass.CartProductId;
import com.poly.repository.ProductDetailRepo;
import com.poly.service.*;
import com.poly.service.Impl.DeliveryNotesImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
//@RequestMapping("/user")
public class CartController {

    @Autowired
    HttpSession session;

    @Autowired
    CartService cartService;

    @Autowired
    CustomerService customerService;

    @Autowired
    BillService billService;

    @Autowired
    ProductService productService;

    @Autowired
    ProductDetailService productDetailService;
    @Autowired
    ProductDetailRepo productDetailRepo;

    @Autowired
    private CartProductService cartProductService;

    @Autowired
    private VoucherCustomerService voucherCustomerService;

    @Autowired
    private CheckLogin checkLogin;

    @Autowired
    private SavePdf savePdf;

    @Autowired
    private SendEmail sendEmail;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private DeliveryNotesImpl deliveryNotes;

    @GetMapping("/pay")
    public String pay(Model model) {
        session.setAttribute("pageView", "/user/page/product/pay.html");
        model.addAttribute("billProduct", new BillProRes());
        List<BigDecimal> listRedu = new ArrayList<>();
        BigDecimal reduceMoney = BigDecimal.valueOf(0);
        BigDecimal total = new BigDecimal(0);
        UserDetailDto userDetailDto = checkLogin.checkLogin();
        List<CartProduct> list = new ArrayList<>();

        if (userDetailDto != null) {
            Cart cart = cartService.getOneByUser(userDetailDto.getId());
            list = cart.getListCartPro();

            List<VoucherCustomer> voucherCustomer = voucherCustomerService.findByUser(userDetailDto.getId());
            model.addAttribute("listVoucher", voucherCustomer.stream().filter(voucherCustomer1 -> voucherCustomer1.isActive() == true && voucherCustomer1.getVoucher().getActive() == true).toList());

            session.setAttribute("list", cart.getListCartPro());
        } else {
            list = (List<CartProduct>) session.getAttribute("list");
        }
        if (list == null || list.isEmpty()) {
            session.setAttribute("pageView", "/user/page/product/cart_null.html");
            session.setAttribute("list", null);
            return "user/index";
        }
        for (CartProduct product : list) {
            if (product.getProduct().getCoupon() != null && product.getProduct().getCoupon().isActive() && product.getProduct().getCoupon().isActive()
                    && ((LocalDate.now().isAfter(product.getProduct().getCoupon().getDateStart().toLocalDate()) || LocalDate.now().isEqual(product.getProduct().getCoupon().getDateStart().toLocalDate()))
                    && (LocalDate.now().isBefore(product.getProduct().getCoupon().getDateEnd().toLocalDate())) || LocalDate.now().isEqual(product.getProduct().getCoupon().getDateEnd().toLocalDate()))) {
                reduceMoney = product.getProduct().getPriceExport().subtract(product.getProduct().getPriceExport().multiply(new BigDecimal(product.getProduct().getCoupon().getValue()).divide(new BigDecimal(100))));
                total = total.add(reduceMoney.multiply(BigDecimal.valueOf(product.getQuantity())));
                listRedu.add(reduceMoney);
            } else {
                reduceMoney = product.getProduct().getPriceExport();
                total = total.add(product.getProduct().getPriceExport().multiply(BigDecimal.valueOf(product.getQuantity())));
                listRedu.add(reduceMoney);
            }
        }

        model.addAttribute("cartProduct",list);

        model.addAttribute("reduceMoney", listRedu);
        model.addAttribute("total", total);
        return "user/index";
    }

    @GetMapping("/confirm")
    public String con(Model model) {
//        model.addAttribute("listBill", new Bill());
        String code = (String) session.getAttribute("listBill");
        Optional<Bill> optional = billService.findByCode(code);
        if (optional.isPresent()) {
            Bill bill = optional.get();
            session.setAttribute("listBill", bill);

            try {
                byte[] file = savePdf.generatePdf(optional.get());
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
        }
        session.setAttribute("pageView", "/user/page/product/confirm.html");
        return "user/index";
    }

    @PreAuthorize("hasAuthority('USER') or isAnonymous()")
    @PostMapping("/purchase")
    public String addBill(@ModelAttribute(value = "billProduct") BillProRes billProRes, BindingResult result,
                          HttpServletRequest request,
                          Model model,BigDecimal deliveryFee,
                          String city, String district, String ward, RedirectAttributes redirectAttributes
    ) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        //
        Users checkEmail = customerService.findByEmail(billProRes.getEmail());
        UserDetailDto userDetailDto = checkLogin.checkLogin();
        List<CartProduct> listCart = new ArrayList<>();
        BigDecimal reduceMoney = BigDecimal.valueOf(0);
        listCart = (List<CartProduct>) session.getAttribute("list");

        List<BigDecimal> listRedu = new ArrayList<>();
        List<Integer> listPro = new ArrayList<>();
        List<Integer> quantity = new ArrayList<>();
        BigDecimal total = new BigDecimal(0);
        for (CartProduct product : listCart) {
            if (product.getProduct().getCoupon() != null && product.getProduct().getCoupon().isActive()
                    && ((LocalDate.now().isAfter(product.getProduct().getCoupon().getDateStart().toLocalDate()) || LocalDate.now().isEqual(product.getProduct().getCoupon().getDateStart().toLocalDate()))
                    && (LocalDate.now().isBefore(product.getProduct().getCoupon().getDateEnd().toLocalDate()) || LocalDate.now().isEqual(product.getProduct().getCoupon().getDateEnd().toLocalDate())))) {
                reduceMoney = product.getProduct().getPriceExport().multiply(new BigDecimal(product.getProduct().getCoupon().getValue()).divide(new BigDecimal(100)));
                listRedu.add(reduceMoney);
                total = total.add(product.getProduct().getPriceExport().subtract(reduceMoney).multiply(BigDecimal.valueOf(product.getQuantity())));
            } else {
                total = total.add(product.getProduct().getPriceExport().multiply(BigDecimal.valueOf(product.getQuantity())));
                listRedu.add(BigDecimal.valueOf(0));
            }
            quantity.add(product.getQuantity());
            listPro.add(product.getProduct().getId());
        }
        billProRes.setTotalPrice(total);// lấy tổng tiền
        if (userDetailDto == null) {
            if (checkEmail == null) {
                checkEmail = customerService.add(billProRes);
                billProRes.setCustomer(checkEmail);
            } else if (checkEmail != null && checkEmail.getRoles() == null) {
                billProRes.setCustomer(checkEmail);
            } else if (checkEmail != null && checkEmail.getRoles().equals("USER")) {
                redirectAttributes.addFlashAttribute("message", "emailTrung");
                return "redirect:/pay";// thông báo email đã dùng đăng ký tài khoản
            }
        } else {
            billProRes.setCustomer(customerService.findByEmail(userDetailDto.getEmail()));
            billProRes.setEmail(userDetailDto.getEmail());
        }
        billProRes.setAddress(billProRes.getAddress() + ", " + ward + ", " + district + ", " + city);
//        billProRes.setDeliveryFee(deliveryFee);// phi giao hang
        Bill bill1 = billService.add(billProRes);// tạo hóa đơn mới
        billProRes.setBill(bill1);
        DeliveryNotes notes = deliveryNotes.save(billProRes);// tạo phiếu giao hàng
        billProRes.setQuantity(quantity);
        billProRes.setReducedMoney(listRedu);// lấy danh sách giảm giá
        billProRes.setProduct(listPro);// lấy danh sách sản phẩm

        if (billProRes.getPaymentMethod() == 2) {
            //VNPAY
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder(bill1.getTotalPrice().add(billProRes.getDeliveryFee()), bill1.getCode(), baseUrl);
            billService.addBillPro(bill1, billProRes);

            if (userDetailDto != null) {
                Cart cart = cartService.getOneByUser(userDetailDto.getId());
                for (CartProduct cartProduct : cart.getListCartPro()) {
                    CartProductId cartProductId = new CartProductId();
                    cartProductId.setProduct(cartProduct.getProduct());
                    cartProductId.setCart(cart);
                    boolean check = cartProductService.delete(cartProductId);
                }
                session.setAttribute("list", null);
            }
            cartService.clear();
            return "redirect:" + vnpayUrl;
        } else {
            billService.addBillPro(bill1, billProRes);
            //
            for (CartProduct item : listCart) {
                ProductDetail product = item.getProduct();
                int newQuantity = product.getQuantity() - item.getQuantity();
                if (newQuantity < 0) {
                    product.setActive(false);
                    model.addAttribute("error", "Không đủ hàng cho sản phẩm: " + product.getProduct().getNameProduct());
                    return "checkout";
                }
                product.setQuantity(newQuantity);
                productDetailRepo.save(product);
            }
            // update sl
            if (userDetailDto != null) {
                Cart cart = cartService.getOneByUser(userDetailDto.getId());
                for (CartProduct cartProduct : cart.getListCartPro()) {
                    CartProductId cartProductId = new CartProductId();
                    cartProductId.setProduct(cartProduct.getProduct());
                    cartProductId.setCart(cart);
                    boolean check = cartProductService.delete(cartProductId);
                }
                session.setAttribute("list", null);
            }

            session.setAttribute("listBill", bill1.getCode());
            cartService.clear();
            return "redirect:/confirm";
        }
    }

    @GetMapping("/cart")
    public String index(Model model) {
        session.setAttribute("pageView", "/user/page/product/pro_cart.html");
        UserDetailDto userDetailDto = checkLogin.checkLogin();
        List<CartProduct> list = new ArrayList<>();

        if (userDetailDto != null) {
            Cart cart = cartService.getOneByUser(userDetailDto.getId());
//            list = cart.getListCartPro();

            CartProduct cartProduct = new CartProduct();

            for (int i = 0; i < cart.getListCartPro().size(); i++) {
                cartProduct = cart.getListCartPro().get(i);
                ProductDetail productDetail = productDetailService.findById(cart.getListCartPro().get(i).getProduct().getId());
                if (cart.getListCartPro().get(i).getQuantity() > productDetail.getQuantity()) {

                    cartProduct.setQuantity(productDetail.getQuantity());
                    cartProduct = cartProductService.update(cartProduct);
                }
                list.add(cartProduct);
            }
            session.setAttribute("list", list);
        } else {
            list = (List<CartProduct>) session.getAttribute("list");
            CartProduct cartProduct = new CartProduct();
            if (session.getAttribute("list") != null) {
                if (list.isEmpty() == false) {
                    for (int i = 0; i < list.size(); i++) {
                        cartProduct = list.get(i);
                        ProductDetail productDetail = productDetailService.findById(list.get(i).getProduct().getId());
                        if (list.get(i).getQuantity() > productDetail.getQuantity()) {
                            cartProduct.setQuantity(productDetail.getQuantity());
                            list = cartService.update(productDetail.getId(), productDetail.getQuantity());
//                            list.add(cartProduct);
                        }
                    }
                }
            }
            session.setAttribute("list", list);
        }


        if (list == null || list.isEmpty()) {
            session.setAttribute("pageView", "/user/page/product/cart_null.html");
            session.setAttribute("list", null);
            return "user/index";
        }


        model.addAttribute("reduceMoney", cartService.getListReduceMoney(list));
        model.addAttribute("total", cartService.getTotalNoLogin(list));
        return "user/index";
    }

    @RequestMapping("/cart/remove/{id}")
    public String delete(@PathVariable List<Integer> id, RedirectAttributes redirectAttributes) {
        UserDetailDto userDetailDto = checkLogin.checkLogin();
        if (userDetailDto != null) {
            Cart cart = cartService.getOneByUser(userDetailDto.getId());
            session.setAttribute("list", cart.getListCartPro());
            ProductDetail productDetail = productDetailService.findById(id.get(0));
            CartProductId cartProductId = new CartProductId();
            if (productDetail != null) {
                cartProductId.setProduct(productDetail);
                cartProductId.setCart(cart);
            }
            boolean check = cartProductService.delete(cartProductId);
            session.setAttribute("list", cartService.getOneByUser(userDetailDto.getId()).getListCartPro());
        } else {
            List<CartProduct> list = new ArrayList<>();
            for (int i = 0; i < id.size(); i++) {
                list = cartService.delete(id.get(i));
            }
            session.setAttribute("list", list);
        }
        redirectAttributes.addFlashAttribute("message", "xoathanhcong");
        return "redirect:/cart";
    }


    @PostMapping("/cart/update")
    public String update(@RequestParam(value = "id", required = false) List<Integer> id, @RequestParam("qty") List<Integer> qty, Model model, RedirectAttributes redirectAttributes) {
        UserDetailDto userDetailDto = checkLogin.checkLogin();
        if (userDetailDto != null) {
            Cart cart = cartService.getOneByUser(userDetailDto.getId());

            List<CartProduct> list = new ArrayList<>();

            for (int i = 0; i < id.size(); i++) {
                ProductDetail productDetail = productDetailService.findById(id.get(i));
                CartProductId cartProductId = new CartProductId();
                cartProductId.setCart(cart);
                cartProductId.setProduct(productDetail);
                Optional<CartProduct> optional = cartProductService.getOne(cartProductId);
                if (optional.isPresent()) {
                    CartProduct cartProduct = optional.get();
                    cartProduct.setQuantity(qty.get(i));

                    //update
                    if (productDetail != null) {
                        if (qty.get(i) > productDetail.getQuantity()) {
                            redirectAttributes.addFlashAttribute("message", false);
                            return "redirect:/cart";
                        } else if (qty.get(i) < 1) {
                            redirectAttributes.addFlashAttribute("message", "nhohon1");
                            return "redirect:/cart";
                        } else if (qty.get(i) > 10) {
                            redirectAttributes.addFlashAttribute("message", "qua10");
                            return "redirect:/cart";
                        } else {
                            list.add(cartProductService.update(cartProduct));
                            session.setAttribute("list", list);
                        }
                    } else {
                        redirectAttributes.addFlashAttribute("message", false);
                    }
                }
            }
            session.setAttribute("list", list);
        } else {
            List<CartProduct> list = new ArrayList<>();
            for (int i = 0; i < id.size(); i++) {
                ProductDetail productDetail = productDetailService.findById(id.get(i));
                if (productDetail != null) {
                    if (qty.get(i) > productDetail.getQuantity()) {
                        redirectAttributes.addFlashAttribute("message", false);
                        return "redirect:/cart";
                    } else if (qty.get(i) < 1) {
                        redirectAttributes.addFlashAttribute("message", "nhohon1");
                        return "redirect:/cart";
                    } else if (qty.get(i) > 10) {
                        redirectAttributes.addFlashAttribute("message", "qua10");
                        return "redirect:/cart";
                    } else {
                        list = cartService.update(id.get(i), qty.get(i));
                        session.setAttribute("list", list);
                    }
                } else {
                    redirectAttributes.addFlashAttribute("message", false);
                }
            }
        }
        redirectAttributes.addFlashAttribute("message", "update-success");
        return "redirect:/cart";
    }
}
