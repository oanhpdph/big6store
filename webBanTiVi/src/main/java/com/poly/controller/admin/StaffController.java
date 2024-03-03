package com.poly.controller.admin;

import com.poly.common.UploadFile;
import com.poly.dto.SearchStaffDto;
import com.poly.entity.Users;
import com.poly.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class StaffController {


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CustomerService customerService;

    @GetMapping("/staff")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String loadStaff(HttpSession session, Model model,
                            @RequestParam(name = "page", required = false, defaultValue = "1") Integer pageRequest,
                            @RequestParam(name = "size", required = false, defaultValue = "2") Integer sizeRequest,
                            @ModelAttribute(name = "search") SearchStaffDto search
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
        Page<Users> staffPage = customerService.loadData(search, pageable);

        model.addAttribute("totalElements", staffPage.getTotalElements());
        session.setAttribute("list", staffPage);
        session.setAttribute("pageView", "/admin/page/staff/staff.html");
        session.setAttribute("active", "/staff/list");
        return "admin/layout";
    }


    @PostMapping("/staff/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addStaff(Model model,
                           HttpSession session,
                           @Valid @ModelAttribute("staff") Users staff, BindingResult bindingResult,
                           @RequestParam("image") MultipartFile file
    ) {

        if (bindingResult.hasErrors()) {
            return "admin/layout";
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // xóa ký tự đặc biệt
        staff.setAvatar(fileName);
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        this.customerService.save(staff);
        String uploadDir = "src/main/resources/static/image"; // đường dẫn upload
        try {
            UploadFile.saveFile(uploadDir, fileName, file);
        } catch (IOException e) {
            //
            e.printStackTrace();
        }
        return "redirect:/admin/staff";
    }

    @GetMapping("/staff/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Users staff = customerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("listStaff", customerService.findAll());
        model.addAttribute("staff", staff);
        return "redirect:/admin/staff";
    }

    @PostMapping("/staff/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateUser(@PathVariable("id") Integer id,
                             HttpSession session,
                             @ModelAttribute("staff") Users staff, Model model,
                             @RequestParam("image") MultipartFile file
    ) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // xóa ký tự đặc biệt
        Users findStaff = customerService.findById(staff.getId()).orElse(null);
        findStaff.setUsername(staff.getUsername());
        findStaff.setName(staff.getName());
        findStaff.setEmail(staff.getEmail());
        findStaff.setGender(staff.isGender());
        findStaff.setPassword(staff.getPassword());
        findStaff.setAddress(staff.getAddress());
        findStaff.setRoles(staff.getRoles());
        findStaff.setBirthday(staff.getBirthday());
        if (!"".equals(fileName)) {
            findStaff.setAvatar(fileName);
            String uploadDir = "src/main/resources/static/image"; // đường dẫn upload
            try {
                UploadFile.saveFile(uploadDir, fileName, file);
            } catch (IOException e) {
                //
                e.printStackTrace();
            }
        }
        String password = staff.getPassword();
        if (password.startsWith("$2a$") && password.contains("$")) {
            findStaff.setPassword(password);
        } else {
            findStaff.setPassword(passwordEncoder.encode(password));
        }
        this.customerService.save(findStaff);
        return "redirect:/admin/staff";
    }

    @GetMapping("/staff/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        Users staff = customerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        customerService.deleteById(id);
        return "redirect:/admin/staff";
    }
}
