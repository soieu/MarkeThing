package com.example.demo.payment.service;

import com.example.demo.entity.MarketPurchaseRequest;
import com.example.demo.payment.dto.PaymentCallbackRequest;
import com.example.demo.payment.dto.RequestPayDto;
import com.example.demo.payment.entity.Payment;
import com.example.demo.payment.repository.MarketPurchaseRequestRepository;
import com.example.demo.payment.repository.PaymentRepository;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.type.PaymentStatus;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.anyString;



class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private IamportClient iamportClient;
    @Mock
    private MarketPurchaseRequestRepository marketPurchaseRequestRepository;

    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentServiceImpl(paymentRepository, iamportClient, marketPurchaseRequestRepository);
    }

    @Test
    void testFindRequestDto() {
        // Given
        String orderUid = "test-order-uid";
        MarketPurchaseRequest mockRequest = mock(MarketPurchaseRequest.class);
        com.example.demo.payment.entity.Payment mockPayment = mock(com.example.demo.payment.entity.Payment.class);
        SiteUser mockUser = mock(SiteUser.class);

        when(marketPurchaseRequestRepository.findOrderAndPaymentAndMember(orderUid))
                .thenReturn(Optional.of(mockRequest));
        when(mockRequest.getSiteUser()).thenReturn(mockUser);
        when(mockRequest.getPayment()).thenReturn(mockPayment);
        when(mockUser.getName()).thenReturn("Test User");
        when(mockPayment.getAmount()).thenReturn(10000L);
        when(mockRequest.getTitle()).thenReturn("Test Item");
        when(mockRequest.getOrderUid()).thenReturn(orderUid);

        // When
        RequestPayDto result = paymentService.findRequestDto(orderUid);

        // Then
        assertNotNull(result);
        assertEquals("Test User", result.getBuyerName());
        assertEquals(10000L, result.getPaymentPrice());
        assertEquals("Test Item", result.getItemName());
        assertEquals(orderUid, result.getOrderUid());
    }

    @Test
    void testPaymentByCallback_Success() throws IamportResponseException, IOException {
        // Given
        PaymentCallbackRequest request = new PaymentCallbackRequest();
        IamportResponse<com.siot.IamportRestClient.response.Payment> mockIamportResponse = mock(IamportResponse.class);
        com.siot.IamportRestClient.response.Payment mockPayment = mock(com.siot.IamportRestClient.response.Payment.class);
        MarketPurchaseRequest mockRequest = mock(MarketPurchaseRequest.class);
        com.example.demo.payment.entity.Payment mockOrderPayment = mock(com.example.demo.payment.entity.Payment.class);

        when(iamportClient.paymentByImpUid(request.getPaymentUid())).thenReturn(mockIamportResponse);
        when(mockIamportResponse.getResponse()).thenReturn(mockPayment);
        when(mockPayment.getStatus()).thenReturn("paid");
        when(mockPayment.getAmount()).thenReturn(new BigDecimal(10000));
        when(marketPurchaseRequestRepository.findOrderAndPayment(request.getOrderUid()))
                .thenReturn(Optional.of(mockRequest));
        when(mockRequest.getPayment()).thenReturn(mockOrderPayment);
        when(mockOrderPayment.getAmount()).thenReturn(10000L);

        // When
        IamportResponse<com.siot.IamportRestClient.response.Payment> result = paymentService.paymentByCallback(request);

        // Then
        assertNotNull(result);
        verify(mockOrderPayment).changePaymentBySuccess();
    }



}