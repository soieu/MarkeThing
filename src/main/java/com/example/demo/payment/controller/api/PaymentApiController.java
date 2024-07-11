package com.example.demo.payment.controller.api;

import com.example.demo.payment.dto.CancelPaymentRequestDto;
import com.example.demo.payment.dto.PaymentCallbackRequestDto;
import com.example.demo.payment.dto.PaymentListRequestDto;
import com.example.demo.payment.entity.Pay;
import com.example.demo.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentApiController {
    @Autowired
    private final PaymentService paymentService;

    @PostMapping
    public void validationPayment(@RequestBody PaymentCallbackRequestDto request) {
        paymentService.paymentByCallback(request);
    }

    @PostMapping("/{paymentId}/cancel")
    public void cancelPayment(@PathVariable Long paymentId,
                              @RequestBody CancelPaymentRequestDto request) {
        paymentService.cancelPayment(paymentId, request);
    }

    @PostMapping("/list")
    public ResponseEntity<List<Pay>> getPaymentList(@RequestBody PaymentListRequestDto paymentListRequestDto) {
        var result = paymentService.listPayment(paymentListRequestDto);
        return ResponseEntity.ok(result);
    }
}