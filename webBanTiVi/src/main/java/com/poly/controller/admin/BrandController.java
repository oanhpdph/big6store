package com.poly.controller.admin;

import com.poly.entity.Brand;
import com.poly.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping("/brand/all")
    public ResponseEntity<?> findAllBrand() {
        List<Brand> list = brandService.findAll().stream().filter(brand -> brand.isActive() == true).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/brand")
    public ResponseEntity<?> getAll() {
        List<Brand> list = brandService.findAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/brand/find-id")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        Brand brand = brandService.findById(id);
        return ResponseEntity.ok(brand);
    }

    @PostMapping("/brand/update")
    public ResponseEntity<?> update(@RequestBody Brand brand) {
        Brand list = brandService.update(brand);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/brand/save")
    public ResponseEntity<?> save(@RequestBody Brand brand) {
        Brand list = brandService.save(brand);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/brand/find-name")
    public ResponseEntity<?> findName(@RequestParam String name) {
        Brand brand = brandService.findName(name);
        return ResponseEntity.ok(brand);
    }
}
