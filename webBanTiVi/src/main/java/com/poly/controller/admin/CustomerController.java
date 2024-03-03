package com.poly.controller.admin;

import com.poly.common.UploadFile;
import com.poly.dto.SearchStaffDto;
import com.poly.entity.Users;
import com.poly.service.CustomerService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class CustomerController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CustomerService customerService;


    @GetMapping("/customer/list")
    public String loadStaff(HttpSession session, Model model,
                            @RequestParam(name = "page", required = false, defaultValue = "1") Integer pageRequest,
                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer sizeRequest,
                            @ModelAttribute(name = "searchCustomer") SearchStaffDto search
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
        Page<Users> staffs = customerService.loadData(search, pageable);

        model.addAttribute("countStaff", customerService.loadData(new SearchStaffDto("", "STAFF"), pageable).getContent().stream().filter(users -> !users.getRoles().equals("USER")).toList().size());
        model.addAttribute("countUser", customerService.loadData(new SearchStaffDto("", "USER"), pageable).getContent().stream().filter(users -> users.getRoles().equals("USER")).toList().size());
        model.addAttribute("totalElements", staffs.getTotalElements());
        session.setAttribute("list", staffs);

        session.setAttribute("pageView", "/admin/page/customer/customer.html");
        session.setAttribute("active", "/customer/list");
        return "admin/layout";
    }

    @PostMapping("/customer/save")
    public String addCustomer( @ModelAttribute("customer") Users customer, Model model,
                              @RequestParam("image") MultipartFile file
    ) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // xóa ký tự đặc biệt
        customer.setAvatar(fileName);
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        this.customerService.save(customer);
        String uploadDir = "src/main/resources/static/image"; // đường dẫn upload
        try {
            UploadFile.saveFile(uploadDir, fileName, file);
        } catch (IOException e) {
            //
            e.printStackTrace();
        }
        model.addAttribute("listCus", customerService.findAll());
        return "redirect:/admin/customer/list";
    }

    @GetMapping("/customer/edit/{id}")
    public String showUpdateCustomer(@PathVariable("id") Integer id, Model model) {
        Users cus = customerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("customer", cus);
        model.addAttribute("listCus", customerService.findAll());
        return "admin/layout";
    }

    @PostMapping("/customer/update/{id}")
    public String updateCustomer(@PathVariable("id") Integer id,
                                 @ModelAttribute("customer") Users customer,
                                 @RequestParam("image") MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename()); // xóa ký tự đặc biệt
        Users findCustomer = this.customerService.findById(id).orElse(null);

        findCustomer.setBirthday(customer.getBirthday());
        findCustomer.setName(customer.getName());
        findCustomer.setEmail(customer.getEmail());
        findCustomer.setAddress(customer.getAddress());
        findCustomer.setPhoneNumber(customer.getPhoneNumber());
        findCustomer.setGender(customer.isGender());
        findCustomer.setRoles(customer.getRoles());
        findCustomer.setStatus(customer.isStatus());
        System.out.println(customer.getAvatar());
        if (!"".equals(fileName)) {
            findCustomer.setAvatar(fileName);
            String uploadDir = "src/main/resources/static/image"; // đường dẫn upload
            try {
                UploadFile.saveFile(uploadDir, fileName, file);
            } catch (IOException e) {
                //
                e.printStackTrace();
            }
        }
        String password = customer.getPassword();
        if (password.startsWith("$2a$") && password.contains("$")) {
            findCustomer.setPassword(password);
        } else {
            findCustomer.setPassword(passwordEncoder.encode(password));
        }
        customerService.save(findCustomer);
        return "redirect:/admin/customer/list";
    }

    @GetMapping("/customer/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Integer id, Model model) {
        Users customer = customerService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        customerService.deleteById(id);
        return "redirect:/admin/customer/list";
    }
}
