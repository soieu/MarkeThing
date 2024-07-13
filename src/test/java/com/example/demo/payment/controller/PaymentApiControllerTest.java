package com.example.demo.payment.controller;

import com.example.demo.payment.controller.api.PaymentApiController;
import com.example.demo.payment.dto.CancelPaymentRequestDto;
import com.example.demo.payment.dto.PayDetailDto;
import com.example.demo.payment.dto.PayResponseDto;
import com.example.demo.payment.dto.PaymentCallbackRequestDto;
import com.example.demo.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PaymentApiControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentApiController paymentApiController;

    private PaymentCallbackRequestDto callbackRequestDto;
    private CancelPaymentRequestDto cancelRequestDto;

    @BeforeEach
    void setUp() {
        PaymentCallbackRequestDto callbackRequestDto = PaymentCallbackRequestDto.builder()
                .paymentUid(1L)
                .orderUid(100L)
                .build();

        CancelPaymentRequestDto cancelRequestDto = CancelPaymentRequestDto.builder()
                .amount(50000)
                .reason("고객 요청")
                .build();
    }

    @Test
    @DisplayName("결제 콜백 처리 테스트")
    void testValidationPayment() {
        // given

        // when
        paymentApiController.validationPayment(callbackRequestDto);

        // then
        verify(paymentService).paymentByCallback(eq(callbackRequestDto));
    }

    @Test
    @DisplayName("결제 취소 처리 테스트")
    void testCancelPayment() {
        // given
        Long paymentId = 1L;

        // when
        paymentApiController.cancelPayment(paymentId, cancelRequestDto);

        // then
        verify(paymentService).cancelPayment(eq(paymentId), eq(cancelRequestDto));
    }

    @Test
    @DisplayName("결제 목록 조회 성공 테스트")
    void testGetPaymentList() {
        // given
        String username = "testUser";
        given(paymentService.listPayment(username))
                .willReturn(Collections.singletonList(new PayResponseDto()));

        // when
        ResponseEntity<List<PayResponseDto>> responseEntity = paymentApiController.getPaymentList(() -> username);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().size());
    }

    @Test
    @DisplayName("결제 상세 조회 성공 테스트")
    void testPaymentDetailSuccess() {
        // given
        Long paymentId = 1L;
        String username = "testUser";
        PayDetailDto payDetailDto = new PayDetailDto();

        given(paymentService.detailPayment(paymentId, username))
                .willReturn(payDetailDto);

        // when
        ResponseEntity<?> responseEntity = paymentApiController.paymentDetail(paymentId, () -> username);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("결제 상세 조회 실패 테스트 - 결제 정보 없음")
    void testPaymentDetailNotFound() {
        // given
        Long paymentId = 1L;
        String username = "testUser";

        given(paymentService.detailPayment(paymentId, username))
                .willReturn(null);

        // when
        ResponseEntity<PayDetailDto> responseEntity = paymentApiController.paymentDetail(paymentId, () -> username);

        // then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
}
