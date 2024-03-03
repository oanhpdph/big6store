package com.poly.controller.user;

import com.poly.common.CheckLogin;
import com.poly.dto.CartDto;
import com.poly.dto.EvaluateRes;
import com.poly.dto.UserDetailDto;
import com.poly.entity.*;
import com.poly.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductDetailUserController {
    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    HttpSession session;

    @Autowired
    CartService cartService;

    @Autowired
    CartProductService cartProductService;

    @Autowired
    CustomerService customerService;

    @Autowired
    EvaluateService evaluateService;

    @Autowired
    ProductService productService;

    @Autowired
    private BillService billService;

    @Autowired
    private CheckLogin checkLogin;

    @GetMapping("/product/detail/{id}")
    public String edit(@PathVariable Integer id, Model model, HttpSession session) {
        ProductDetail product = productDetailService.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("countEvaluate", product.getProduct().getListEvaluate().stream().filter(evaluate -> evaluate.isActive() == true).toList().size());

        model.addAttribute("productAll", product.getProduct());

        BigDecimal reduceMoney = productService.getReduceMoney(product);
        model.addAttribute("reduceMoney", reduceMoney);
        session.setAttribute("pageView", "/user/page/product/detail.html");
        model.addAttribute("listPro", this.productDetailService.findAll());
        UserDetailDto userDetailDto = checkLogin.checkLogin();

        List<Evaluate> evaluates = product.getProduct().getListEvaluate();
        model.addAttribute("hasEvaluate", false);
        if (userDetailDto != null) {
            for (Evaluate evaluate : evaluates) {
                if (evaluate.getCustomer().getId() == userDetailDto.getId()) {
                    model.addAttribute("hasEvaluate", true);
                    break;
                } else {
                    model.addAttribute("hasEvaluate", false);
                }
            }
            List<Bill> bills = findAllByUser(userDetailDto.getId());
            model.addAttribute("hasBuy", false);
            if (!bills.isEmpty()) {
                for (Bill bill : bills) {
                    for (BillProduct billProduct : bill.getBillProducts()) {
                        if (billProduct.getProduct().getId() == id && billProduct.getBill().getBillStatus().getCode().equals("CO")) {
                            model.addAttribute("hasBuy", true);
                        }
                    }
                }
            }
        }
        model.addAttribute("evaluate", evaluates.stream().filter(evaluate -> evaluate.isActive() == true).toList());
        return "user/index";
    }

    @PostMapping("/product/detail/{id}")
    public String add(@PathVariable Integer id, @RequestParam("qty") Integer qty, HttpSession session, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String url = request.getRequestURI();
        ProductDetail productDetail = productDetailService.findById(id);
        List<CartProduct> list = new ArrayList<>();
        UserDetailDto userDetailDto = checkLogin.checkLogin();
        boolean check = false;
        if (session.getAttribute("list") != null) {
            list = (List<CartProduct>) session.getAttribute("list");
            if (list.isEmpty() == false) {// list có phần tử
                for (CartProduct cartProduct : list) {
                    if (cartProduct.getProduct().getId() == id) {
                        if (cartProduct.getQuantity() + qty <= 10) {
                            if (cartProduct.getQuantity() + qty > productDetail.getQuantity()) {
                                redirectAttributes.addFlashAttribute("message", false);
                                return "redirect:" + url;
                            } else {
//                                session.setAttribute("list", cartService.add(id, qty));
                                check = true;
                                break;
                            }
                        } else if (cartProduct.getQuantity() + qty > 10) {
                            redirectAttributes.addFlashAttribute("message", "qua10");
                            return "redirect:" + url;
                        } else {
                            redirectAttributes.addFlashAttribute("message", false);
                            return "redirect:" + url;
                        }
                    } else {
                        check = true;
                    }
                }
            } else {
                list = (List<CartProduct>) session.getAttribute("list");
                check = true;
                for (CartProduct cartProduct : list) {
                    if (cartProduct.getQuantity() + qty <= 10) {
//                session.setAttribute("list", cartService.add(id, qty));
                        if (qty > productDetail.getQuantity()) {
                            redirectAttributes.addFlashAttribute("message", false);
                            return "redirect:" + url;
                        } else {
                            check = true;
                        }
                    } else {
                        redirectAttributes.addFlashAttribute("message", false);
                        return "redirect:" + url;
                    }
                }
            }
        } else {
            if (qty > productDetail.getQuantity()) {
                redirectAttributes.addFlashAttribute("message", false);
                return "redirect:" + url;
            } else {
                check = true;
            }
        }
        if (check == true) {
            List<CartProduct> list1 = new ArrayList<>();

            if (session.getAttribute("list") != null) {
                list1 = (List<CartProduct>) session.getAttribute("list");
            }
            for (CartProduct cartProduct : list1) {
                if (cartProduct.getProduct().getId() == id) {
                    if (qty + cartProduct.getQuantity() > productDetail.getQuantity()) {
                        redirectAttributes.addFlashAttribute("message", false);
                        return "redirect:" + url;
                    }
                } else {
                    if (qty > productDetail.getQuantity()) {
                        redirectAttributes.addFlashAttribute("message", false);
                        return "redirect:" + url;
                    }
                }
            }
            if (userDetailDto != null) {

                CartDto cartDto = new CartDto();
                cartDto.setIdProduct(id);
                cartDto.setIdUser(userDetailDto.getId());
                cartDto.setQuantity(qty);
                CartProduct cartProduct = cartProductService.save(cartDto);
                boolean checkTrung = false;
                for (int i = 0; i < list1.size(); i++) {
                    if (list1.get(i).getProduct().getId() == id) {
                        list1.set(i, cartProduct);
                        checkTrung = true;
                    }
                }
                if (checkTrung == false) {
                    list1.add(cartProduct);
                }
            } else {
                list1 = cartService.add(id, qty);
            }
            session.setAttribute("list", list1);
        }
        redirectAttributes.addFlashAttribute("message", true);
        return "redirect:" + url;
    }


    @GetMapping("/product/evaluate/{id}")
    public String index(@ModelAttribute(name = "evaluate") EvaluateRes evaluateDto, @PathVariable Integer id, Model model, HttpSession httpSession) {
        evaluateDto.setProduct(id);
        evaluateDto.setPage(1);
        evaluateDto.setSize(10000);
        Page<Evaluate> evaluates = evaluateService.getAll(evaluateDto);
        model.addAttribute("list", evaluates);
        UserDetailDto userDetailDto = checkLogin.checkLogin();
        model.addAttribute("hasEvaluate", false);
        if (userDetailDto != null) {
            for (Evaluate evaluate : evaluates.getContent()) {
                if (evaluate.getCustomer().getId() == userDetailDto.getId()) {
                    model.addAttribute("hasEvaluate", true);
                } else {
                    model.addAttribute("hasEvaluate", false);
                }
            }
            List<Bill> bills = findAllByUser(userDetailDto.getId());
            model.addAttribute("hasBuy", false);
            if (!bills.isEmpty()) {
                for (Bill bill : bills) {
                    for (BillProduct billProduct : bill.getBillProducts()) {
                        if (billProduct.getProduct().getProduct().getId() == id && billProduct.getBill().getBillStatus().getCode().equals("CO")) {
                            model.addAttribute("hasBuy", true);
                        }
                    }
                }
            }
        }

        httpSession.setAttribute("pageView", "/user/page/product/evaluate.html");
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("evaluate", evaluates.stream().filter(evaluate -> evaluate.isActive() == true).toList());
        return "user/index";
    }

    public List<Bill> findAllByUser(Integer id) {
        return billService.findAllBillByUser(id);
    }
}
