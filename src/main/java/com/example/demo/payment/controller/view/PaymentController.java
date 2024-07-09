package com.example.demo.payment.controller.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PaymentController {
    @GetMapping("/payment/success")
    public String successPaymentPage() {
        return "success_payment";
    }

    @GetMapping("/payment/fail")
    public String failPaymentPage() {
        return "fail_payment";
    }
}
