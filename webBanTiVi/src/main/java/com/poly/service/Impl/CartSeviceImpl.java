package com.poly.service.Impl;

import com.poly.entity.Cart;
import com.poly.entity.CartProduct;
import com.poly.entity.ProductDetail;
import com.poly.entity.Users;
import com.poly.repository.CartProductRepos;
import com.poly.repository.CartRepos;
import com.poly.repository.ProductDetailRepo;
import com.poly.service.CartService;
import com.poly.service.CustomerService;
import com.poly.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartSeviceImpl implements CartService {
    @Autowired
    CartRepos cartRepos;
    @Autowired
    CartProductRepos cartProductRepos;
    @Autowired
    ProductDetailRepo productDetailRepo;
    @Autowired
    ProductServiceImpl productService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ProductDetailService productDetailService;

    List<CartProduct> items = new ArrayList<>();

    @Override
    public List<CartProduct> getitems() {
        return items;
    }

    @Override
    public List<Cart> getAll() {
        return cartRepos.findAll();
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepos.save(cart);
    }

    @Override
    public List<CartProduct> add(Integer id, Integer qty) {

        CartProduct item = items
                .stream()
                .filter(it -> it.getProduct().getId() == id)
                .findFirst()
                .orElse(null);
        if (item != null) {
            item.setQuantity(item.getQuantity() + qty);
            return items;
        }
        Cart cart = new Cart();
        ProductDetail product = productDetailService.findById(id);
        if (product != null) {
            items.add(
                    new CartProduct(product, cart, qty, null)
            );
        }
        return items;
    }

    @Override
    public List<CartProduct> update(int id, Integer qty) {
        CartProduct item = items
                .stream()
                .filter(it -> it.getProduct().getId() == id)
                .findFirst()
                .orElse(null);
        if (items != null) {
            item.setQuantity(qty);
        }
        return items;
    }

    @Override
    public List<CartProduct> delete(Integer id) {
        items = items
                .stream()
                .filter(it -> it.getProduct().getId() != id)
                .collect(Collectors.toList());
        return items;
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public int getTotal() {
        int total = 0;
        for (CartProduct item : items) {
            total += item.getQuantity();
        }
        return total;
    }

    @Override
    public BigDecimal getAmount() {
        //(sp - sp/100)*qty
        BigDecimal amount = new BigDecimal(0);
        for (CartProduct item : items) {
            if (item.getProduct().getCoupon() != null && item.getProduct().getCoupon().isActive()) {
                BigDecimal values = new BigDecimal(String.valueOf(item.getProduct().getCoupon().getValue()));
                BigDecimal result = values.divide(BigDecimal.valueOf(100));
                BigDecimal price = new BigDecimal(String.valueOf(item.getProduct().getPriceExport()));
                BigDecimal result1 = price.multiply(result);
                BigDecimal result2 = price.subtract(result1);
                BigDecimal qty = new BigDecimal(item.getQuantity());
                BigDecimal redu = result2.multiply(qty);
                amount = amount.add(redu);
            } else {
                BigDecimal qty = new BigDecimal(item.getQuantity());
                BigDecimal price = new BigDecimal(String.valueOf(item.getProduct().getPriceExport()));
                BigDecimal result = qty.multiply(price);
                amount = amount.add(result);
            }
        }
        return amount;
    }

    @Override
    public Cart getOneByUser(Integer id) {
        Optional<Users> user = customerService.findById(id);
        if (user.isPresent()) {
            Optional<Cart> cart = cartRepos.getByUser(user.get());
            if (cart.isPresent()) {
                return cart.get();
            }
        }
        return null;
    }

    @Override
    public BigDecimal getTotalNoLogin(List<CartProduct> list) {
        BigDecimal total = BigDecimal.valueOf(0);
        BigDecimal reduceMoney = BigDecimal.valueOf(0);
        for (CartProduct product : list) {

            if (product.getProduct().getCoupon() != null && product.getProduct().getCoupon().isActive() &&
                    ((LocalDate.now().isAfter(product.getProduct().getCoupon().getDateStart().toLocalDate()) || LocalDate.now().isEqual(product.getProduct().getCoupon().getDateStart().toLocalDate())) &&
                            LocalDate.now().isBefore(product.getProduct().getCoupon().getDateEnd().toLocalDate()) || LocalDate.now().isEqual(product.getProduct().getCoupon().getDateEnd().toLocalDate()))) {
                reduceMoney = product.getProduct().getPriceExport().subtract(product.getProduct().getPriceExport().multiply(new BigDecimal(product.getProduct().getCoupon().getValue()).divide(new BigDecimal(100))));

                total = total.add(reduceMoney.multiply(BigDecimal.valueOf(product.getQuantity())));
            } else {
                reduceMoney = product.getProduct().getPriceExport();

                total = total.add(product.getProduct().getPriceExport().multiply(new BigDecimal(product.getQuantity())));
            }
        }
        return total;
    }

    @Override
    public List<BigDecimal> getListReduceMoney(List<CartProduct> list) {
        List<BigDecimal> listRedu = new ArrayList<>();
        BigDecimal reduceMoney = BigDecimal.valueOf(0);
        for (CartProduct product : list) {
            if (product.getProduct().getCoupon() != null && product.getProduct().getCoupon().isActive() &&
                    ((LocalDate.now().isAfter(product.getProduct().getCoupon().getDateStart().toLocalDate()) || LocalDate.now().isEqual(product.getProduct().getCoupon().getDateStart().toLocalDate())) &&
                            LocalDate.now().isBefore(product.getProduct().getCoupon().getDateEnd().toLocalDate()) || LocalDate.now().isEqual(product.getProduct().getCoupon().getDateEnd().toLocalDate()))) {
                reduceMoney = product.getProduct().getPriceExport().subtract(product.getProduct().getPriceExport().multiply(new BigDecimal(product.getProduct().getCoupon().getValue()).divide(new BigDecimal(100))));
                listRedu.add(reduceMoney);
            } else {
                reduceMoney = product.getProduct().getPriceExport();
                listRedu.add(reduceMoney);
            }
        }
        return listRedu;
    }

}
