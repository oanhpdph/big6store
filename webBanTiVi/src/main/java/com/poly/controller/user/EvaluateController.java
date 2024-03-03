package com.poly.controller.user;

import com.poly.common.CheckLogin;
import com.poly.dto.EvaluateRes;
import com.poly.dto.UserDetailDto;
import com.poly.service.EvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class EvaluateController {

    @Autowired
    private EvaluateService evaluateService;

    @Autowired
    private CheckLogin checkLogin;

    @PostMapping("/product/evaluate/add")
    public ResponseEntity<?> saveEvaluate(@RequestBody EvaluateRes evaluateRes) {
        UserDetailDto userDetailDto = checkLogin.checkLogin();
        if (userDetailDto != null) {
            evaluateRes.setCustomer(userDetailDto.getId());
            evaluateService.add(evaluateRes);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
}
