package com.example.demo.payment.service;

import com.example.demo.payment.dto.*;
import com.example.demo.payment.entity.Pay;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    /**
     * 결제 요청 데이터 조회
     * @param orderUid
     * @return
     */
    RequestPayDto findRequestDto(Long orderUid);

    /**
     * 결제(콜백) 검증
     * @param request
     * @return
     */
    IamportResponse<Payment> paymentByCallback(PaymentCallbackRequestDto request);

    // 결제 취소

    /**
     * 결제 취소
     * @param paymentId
     * @param request
     * @return
     */
    IamportResponse<Payment> cancelPayment(Long paymentId, CancelPaymentRequestDto request);

    /**
     * 결제내역열람
     * @param email
     * @return
     */
    List<PayResponseDto> listPayment(String email);

    /**
     * 상세 결제내역
     *
     * @param id
     * @return
     */
    Optional<PayDetailDto> detailPayment(Long id, String email);
}
