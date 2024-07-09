package com.example.demo.payment.service;

import com.example.demo.exception.MarkethingException;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.repository.MarketPurchaseRequestRepository;
import com.example.demo.payment.dto.CancelPaymentRequest;
import com.example.demo.payment.dto.PaymentCallbackRequest;
import com.example.demo.payment.repository.PaymentRepository;
import com.example.demo.payment.service.impl.PaymentServiceImpl;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

public class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private IamportClient iamportClient;

    @Mock
    private MarketPurchaseRequestRepository marketPurchaseRequestRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPaymentByCallback_SuccessfulPayment() throws IamportResponseException, IOException {
        // Given
        PaymentCallbackRequest request = new PaymentCallbackRequest("123", "123");
        IamportResponse<Payment> iamportResponse = mock(IamportResponse.class);
        Payment payment = mock(Payment.class);
        MarketPurchaseRequest marketPurchaseRequest = mock(MarketPurchaseRequest.class);
        com.example.demo.payment.entity.Payment entityPayment = mock(com.example.demo.payment.entity.Payment.class);

        given(iamportClient.paymentByImpUid(anyString())).willReturn(iamportResponse);
        given(iamportResponse.getResponse()).willReturn(payment);
        given(payment.getStatus()).willReturn("paid");
        given(payment.getAmount()).willReturn(new BigDecimal(10000));
        given(payment.getImpUid()).willReturn("imp123");
        given(marketPurchaseRequestRepository.findById(anyLong())).willReturn(Optional.of(marketPurchaseRequest));
        given(marketPurchaseRequest.getPayment()).willReturn(entityPayment);
        given(entityPayment.getAmount()).willReturn(10000);

        // When
        IamportResponse<Payment> result = paymentService.paymentByCallback(request);

        // Then
        assertNotNull(result);
        then(entityPayment).should().changePaymentBySuccess();
    }


    @Test
    void testPaymentByCallback_PaymentIncomplete() throws IamportResponseException, IOException {
        // Given
        PaymentCallbackRequest request = new PaymentCallbackRequest("123", "123");
        IamportResponse<Payment> iamportResponse = mock(IamportResponse.class);
        Payment payment = mock(Payment.class);
        MarketPurchaseRequest marketPurchaseRequest = mock(MarketPurchaseRequest.class);
        com.example.demo.payment.entity.Payment entityPayment = mock(com.example.demo.payment.entity.Payment.class);

        given(iamportClient.paymentByImpUid(anyString())).willReturn(iamportResponse);
        given(iamportResponse.getResponse()).willReturn(payment);
        given(payment.getStatus()).willReturn("ready");
        given(marketPurchaseRequestRepository.findById(anyLong())).willReturn(Optional.of(marketPurchaseRequest));
        given(marketPurchaseRequest.getPayment()).willReturn(entityPayment);

        // When & Then
        assertThrows(MarkethingException.class, () -> paymentService.paymentByCallback(request));
        then(paymentRepository).should().delete(entityPayment);
    }

    @Test
    void testPaymentByCallback_AmountMismatch() throws IamportResponseException, IOException {
        // Given
        PaymentCallbackRequest request = new PaymentCallbackRequest("123", "123");
        IamportResponse<Payment> iamportResponse = mock(IamportResponse.class);
        Payment payment = mock(Payment.class);
        MarketPurchaseRequest marketPurchaseRequest = mock(MarketPurchaseRequest.class);
        com.example.demo.payment.entity.Payment entityPayment = mock(com.example.demo.payment.entity.Payment.class);

        given(iamportClient.paymentByImpUid(anyString())).willReturn(iamportResponse);
        given(iamportResponse.getResponse()).willReturn(payment);
        given(payment.getStatus()).willReturn("paid");
        given(payment.getAmount()).willReturn(new BigDecimal(10000));
        given(payment.getImpUid()).willReturn("imp123");
        given(marketPurchaseRequestRepository.findById(anyLong())).willReturn(Optional.of(marketPurchaseRequest));
        given(marketPurchaseRequest.getPayment()).willReturn(entityPayment);
        given(entityPayment.getAmount()).willReturn(9000);

        // When & Then
        assertThrows(MarkethingException.class, () -> paymentService.paymentByCallback(request));
        then(paymentRepository).should().delete(entityPayment);
        then(iamportClient).should().cancelPaymentByImpUid(any(CancelData.class));
    }

    @Test
    void testPaymentByCallback_OrderNotExist() throws IamportResponseException, IOException {
        // Given
        PaymentCallbackRequest request = new PaymentCallbackRequest("123", "123");
        given(marketPurchaseRequestRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThrows(MarkethingException.class, () -> paymentService.paymentByCallback(request));
    }

    @Test
    void testCancelPayment_PaymentNotFound() {
        // Given
        String paymentId = "1";
        CancelPaymentRequest request = new CancelPaymentRequest(1000, "TestCancel");

        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(MarkethingException.class, () -> {
            paymentService.cancelPayment(paymentId, request);
        });

        // Then
        assertNotNull(exception);
        verify(paymentRepository, never()).save(any(com.example.demo.payment.entity.Payment.class));
    }

    @Test
    void testCancelPayment_IamportError() throws IamportResponseException, IOException {
        // Given
        String paymentId = "1";
        CancelPaymentRequest request = new CancelPaymentRequest(1000, "Test Cancel");

        com.example.demo.payment.entity.Payment mockPayment = mock(com.example.demo.payment.entity.Payment.class);

        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(mockPayment));
        when(iamportClient.cancelPaymentByImpUid(any(CancelData.class))).thenThrow(IamportResponseException.class);

        // When
        Exception exception = assertThrows(MarkethingException.class, () -> {
            paymentService.cancelPayment(paymentId, request);
        });

        // Then
        assertNotNull(exception);
        verify(paymentRepository, never()).save(any(com.example.demo.payment.entity.Payment.class));
    }
}