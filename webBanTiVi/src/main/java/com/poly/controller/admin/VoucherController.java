package com.poly.controller.admin;

import com.poly.common.UploadFile;
import com.poly.dto.SearchVoucherDto;
import com.poly.entity.Voucher;
import com.poly.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;


    // voucher
    @GetMapping("/voucher/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(voucherService.findAllList().stream().map(Voucher::getCode).collect(Collectors.toList()));
    }

    @GetMapping("/voucher/list")
    public String voucher(HttpSession session, Model model,
                          @RequestParam(name = "page", required = false, defaultValue = "1") Integer pageRequest,
                          @RequestParam(name = "size", required = false, defaultValue = "10") Integer sizeRequest,
                          @ModelAttribute(name = "search") SearchVoucherDto search
    ) {
        if (pageRequest < 0) {
            pageRequest = 1;
        }
        if (sizeRequest <= 0) {
            sizeRequest = 1;
        }

        session.setAttribute("size", sizeRequest);
        session.setAttribute("page", pageRequest);

        Pageable pageable = PageRequest.of(pageRequest - 1, sizeRequest);
        Page<Voucher> vouchers = voucherService.loadData(search, pageable);

        model.addAttribute("totalElements", vouchers.getTotalElements());
        session.setAttribute("listVoucher", vouchers);

        session.setAttribute("pageView", "/admin/page/voucher/voucher.html");
        session.setAttribute("active", "/promotion/voucher");
//        if (vouchers.getTotalPages() < pageRequest) {
//            return "redirect:/admin/voucher/list?page=" + vouchers.getTotalPages() + "&size=" + sizeRequest;
//        }
        return "/admin/layout";

    }

    @PostMapping("/voucher/add")
    public String addVoucher(
            HttpSession session,
            @Valid @ModelAttribute(name = "voucher", binding = true) Voucher voucher,
            BindingResult result,
            @RequestParam("image") MultipartFile file
    ) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        voucher.setImage(fileName);
        this.voucherService.save(voucher);
        String uploadDir = "src/main/resources/static/image"; // đường dẫn upload
        try {
            UploadFile.saveFile(uploadDir, fileName, file);
        } catch (IOException e) {
            //
            e.printStackTrace();
        }
        return "redirect:/admin/voucher/list";
    }

    @GetMapping("/voucher/edit/{id}")
    public String showVoucher(HttpSession session, @PathVariable("id") Integer id, Model model) {
        Voucher voucher = this.voucherService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("voucher", voucher);
//        model.addAttribute("listVoucher", this.voucherService.findAll(pageable));
        return "admin/layout";
    }

    @PostMapping("/voucher/update")
    public ResponseEntity<?> updatevoucher(
                                            @RequestBody Voucher voucher,
//                                           BindingResult result,
//                                           @RequestParam("image") MultipartFile file,
                                           @ModelAttribute(name = "search") SearchVoucherDto search) {
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // xóa ký tự đặc biệt
        Voucher findVoucher = voucherService.findById(voucher.getId()).orElse(null);
        findVoucher.setActive(voucher.getActive());
//        findVoucher.setNameVoucher(voucher.getNameVoucher());
//        findVoucher.setQuantity(voucher.getQuantity());
//        findVoucher.setCode(voucher.getCode());
//        findVoucher.setStartDay(voucher.getStartDay());
//        findVoucher.setExpirationDate(voucher.getExpirationDate());
//        findVoucher.setMinimumValue(voucher.getMinimumValue());
//        findVoucher.setValue(voucher.getValue());

//        if (!"".equals(fileName)) {
//            findVoucher.setImage(fileName);
//            String uploadDir = "src/main/resources/static/image"; // đường dẫn upload
//            try {
//                UploadFile.saveFile(uploadDir, fileName, file);
//            } catch (IOException e) {
//                //
//                e.printStackTrace();
//            }
//        }
        this.voucherService.save(findVoucher);
        return ResponseEntity.ok(200);
    }

    @GetMapping("/voucher/delete/{id}")
    public String deletevoucher(@PathVariable("id") Integer id, Model model) {
        Voucher voucher = this.voucherService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        this.voucherService.delete(id);
        return "redirect:/admin/voucher/list";
    }

    @GetMapping("/voucher/getone")
    public ResponseEntity<?> getOne(@RequestParam("id") Integer id) {
        Optional<Voucher> voucher = this.voucherService.findById(id);
        if (voucher.isPresent()) {
            return ResponseEntity.ok(voucher);
        }
        return ResponseEntity.ok(null);
    }

}
