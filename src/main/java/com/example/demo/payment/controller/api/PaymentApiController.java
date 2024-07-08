package com.example.demo.payment.controller.api;

import com.example.demo.payment.dto.PaymentCallbackRequest;
import com.example.demo.payment.service.PaymentService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentApiController {
    @Autowired
    private final PaymentService paymentService;

    @PostMapping
    public void validationPayment(@RequestBody PaymentCallbackRequest request) {
        IamportResponse<Payment> response = paymentService.paymentByCallback(request);

        log.info("결제 응답={}", response.getResponse().toString());
    }
}