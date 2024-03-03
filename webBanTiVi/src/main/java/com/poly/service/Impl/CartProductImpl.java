package com.poly.service.Impl;

import com.poly.dto.CartDto;
import com.poly.entity.Cart;
import com.poly.entity.CartProduct;
import com.poly.entity.ProductDetail;
import com.poly.entity.Users;
import com.poly.entity.idClass.CartProductId;
import com.poly.repository.CartProductRepos;
import com.poly.service.CartProductService;
import com.poly.service.CartService;
import com.poly.service.CustomerService;
import com.poly.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartProductImpl implements CartProductService {
    @Autowired
    CartService cartService;

    @Autowired
    private CartProductRepos cartProductRepos;

    @Autowired
    CustomerService customerService;

    @Autowired
    ProductDetailService productDetailService;


    @Override
    public List<CartProduct> getAll() {
        return null;
    }

    @Override
    public CartProduct save(CartDto dto) {
        Cart cart = cartService.getOneByUser(dto.getIdUser());
        CartProduct cartProduct = new CartProduct();
        ProductDetail productDetail = productDetailService.findById(dto.getIdProduct());

        if (cart == null) {
            cart = new Cart();
            Users user = customerService.findById(dto.getIdUser()).get();
            if (user != null) {
                cart.setCustomer(user);
            }
            Cart cart1 = cartService.save(cart);
            cartProduct.setCart(cart1);
        } else {
            cartProduct.setCart(cart);
            if(cart.getListCartPro() !=null) {
                for (CartProduct product : cart.getListCartPro()) {
                    if (product.getProduct().getId() == productDetail.getId()) {
                        product.setQuantity(product.getQuantity() + dto.getQuantity());
                        return update(product);
                    }
                }
            }
        }
        cartProduct.setProduct(productDetail);
        cartProduct.setQuantity(dto.getQuantity());
        return cartProductRepos.save(cartProduct);
    }

    @Override
    public CartProduct update(CartProduct cp) {
        return cartProductRepos.save(cp);
    }

    @Override
    public Boolean delete(CartProductId id) {
        Optional<CartProduct> cartProduct = cartProductRepos.findById(id);
        if (cartProduct.isPresent()) {
            cartProductRepos.delete(cartProduct.get());
            return true;
        }
        return false;
    }

    @Override
    public CartProduct edit(CartProductId id) {
        return null;
    }

    @Override
    public Optional<CartProduct> getOne(CartProductId id) {
        Optional<CartProduct> cartProduct = cartProductRepos.findById(id);
        if (cartProduct.isPresent()) {
            return cartProduct;
        }
        return null;
    }
}
