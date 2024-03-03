package com.poly.controller.Error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {
    @GetMapping("/403")
    public String getError() {
        return "login/error403";
    }

    @GetMapping("/404")
    public String error404() {
        return "error/error404";
    }
    @GetMapping("/500")
    public String error500() {
        return "login/error403";
    }

}
