package com.poly.controller.admin;

import com.poly.common.UploadFile;
import com.poly.dto.ProductDetailDto;
import com.poly.dto.ProductDetailListDto;
import com.poly.entity.ProductDetail;
import com.poly.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductDetailController {

    @Autowired
    private ProductDetailService productDetailService;

    @PostMapping("/save-product")
    public ResponseEntity<?> saveProductDetail(@RequestBody ProductDetailDto productDetailDto) {
        return ResponseEntity.ok(productDetailService.saveList(productDetailDto));
    }

    @PostMapping(path = "/upload")
    public ResponseEntity<?> upload(@RequestParam(value = "images", required = false) List<MultipartFile> list) throws IOException {
        if (list != null) {
            for (MultipartFile multipartFile : list) {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    UploadFile.saveFile("src/main/resources/static/image/product", fileName, multipartFile);
            }
        }
        return ResponseEntity.ok(200);
    }

    @GetMapping("/get-one-product")
    public ResponseEntity<?> getOneProduct(@RequestParam("id") Integer id) {
        return ResponseEntity.ok(productDetailService.findById(id));
    }

    @PostMapping("/update-productdetail")
    public ResponseEntity<?> updateProductDetail(@RequestBody ProductDetailListDto productDetailListDto) {

        return ResponseEntity.ok(productDetailService.update(productDetailListDto));
    }

    @GetMapping("/product-detail/all")
    public ResponseEntity<?> getALl() {
        return ResponseEntity.ok(productDetailService.findAll().stream().map(ProductDetail::getSku).collect(Collectors.toList()));
    }
}
