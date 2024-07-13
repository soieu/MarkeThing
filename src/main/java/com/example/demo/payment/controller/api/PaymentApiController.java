package com.example.demo.payment.controller.api;

import com.example.demo.payment.dto.*;
import com.example.demo.payment.entity.Pay;
import com.example.demo.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/list")
    public ResponseEntity<List<PayResponseDto>> getPaymentList(Principal principal) {
        var result = paymentService.listPayment(principal.getName());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/list/{paymentId}")
    public ResponseEntity<?> paymentDetail(@PathVariable Long paymentId, Principal principal) {
        Optional<PayDetailDto> result = paymentService.detailPayment(paymentId, principal.getName());
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}