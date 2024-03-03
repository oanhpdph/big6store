package com.poly.controller.user;

import com.poly.entity.Voucher;
import com.poly.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class PayController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping("/voucher/getone")
    public ResponseEntity<?> getOne(@RequestParam("id") Integer id) {
        Optional<Voucher> voucher = this.voucherService.findById(id);
        if (voucher.isPresent()) {
            return ResponseEntity.ok(voucher);
        }
        return ResponseEntity.ok(null);
    }

    @GetMapping("/ship/getprice")
    public ResponseEntity<?> getShip() {
        return ResponseEntity.ok(null);
    }
}
