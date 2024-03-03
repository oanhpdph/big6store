package com.poly.controller.admin;

import com.poly.service.GroupProductService;
import com.poly.service.Impl.GroupProductImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/group")
public class GroupProductController {

    @Autowired
    private GroupProductService groupProduct;

    @GetMapping("/all")
    public ResponseEntity<?> getDataGroup() {
        return ResponseEntity.ok(groupProduct.findAll());
    }
}
