package com.example.demo.payment.controller.api;

import com.example.demo.payment.dto.CancelPaymentRequestDto;
import com.example.demo.payment.dto.PaymentCallbackRequestDto;
import com.example.demo.payment.service.PaymentService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentApiController {
    @Autowired
    private final PaymentService paymentService;

    @PostMapping
    public void validationPayment(@RequestBody PaymentCallbackRequestDto request) {
        IamportResponse<Payment> response = paymentService.paymentByCallback(request);
    }

    @PostMapping("/{paymentId}/cancel")
    public void cancelPayment(@PathVariable Long paymentId,
                              @RequestBody CancelPaymentRequestDto request) {
        IamportResponse<Payment> response = paymentService.cancelPayment(paymentId, request);
    }
}