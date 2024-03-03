package com.poly.controller.admin;

import com.poly.dto.ProductDetailDto;
import com.poly.dto.ProductDetailListDto;
import com.poly.entity.Product;
import com.poly.service.ProductDetailService;
import com.poly.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDetailService productDetailService;

    @PostMapping(path = "/update-product")
    public ResponseEntity<?> updateProudct(@RequestBody ProductDetailDto productDetailDto) {
        return ResponseEntity.ok(productService.update(productDetailDto.getId(), productDetailDto));
    }

    @GetMapping(path = "/get-product-by-id")
    public ResponseEntity<?> getById(@RequestParam("id") Integer id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping("/update-active-detail")
    public ResponseEntity<?> updateActiveProductDetail(@RequestBody List<ProductDetailListDto> list) {
        for (ProductDetailListDto productDetailListDto : list) {
            productDetailService.update(productDetailListDto);
        }
        return ResponseEntity.ok(200);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getALl() {
        List<Product> productList = productService.findAll();
        return ResponseEntity.ok(productList);
    }
}
