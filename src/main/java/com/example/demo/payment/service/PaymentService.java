package com.example.demo.payment.service;

import com.example.demo.payment.dto.PaymentCallbackRequest;
import com.example.demo.payment.dto.RequestPayDto;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

public interface PaymentService {
    // 결제 요청 데이터 조회
    RequestPayDto findRequestDto(String orderUid);

    // 결제(콜백) 검증
    IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request);
}
