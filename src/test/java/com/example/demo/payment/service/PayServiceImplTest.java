package com.example.demo.payment.service;

import com.example.demo.exception.MarkethingException;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.marketpurchaserequest.repository.MarketPurchaseRequestRepository;
import com.example.demo.payment.dto.*;
import com.example.demo.payment.entity.Pay;
import com.example.demo.payment.repository.PaymentRepository;
import com.example.demo.payment.service.impl.PaymentServiceImpl;
import com.example.demo.siteuser.entity.SiteUser;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.PaymentStatus;
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
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.demo.exception.type.ErrorCode.SUSPECT_PAYMENT_FORGERY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

public class PayServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private IamportClient iamportClient;

    @Mock
    private MarketPurchaseRequestRepository marketPurchaseRequestRepository;

    @Mock
    private SiteUserRepository siteUserRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SiteUser siteUser;

    @Mock
    private PayDetailDto payDetailDto;

    @Mock
    private Pay pay;

    @Mock
    private Principal principal;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPaymentByCallback_SuccessfulPayment() throws IamportResponseException, IOException {

    }


    @Test
    void testPaymentByCallback_PaymentIncomplete() throws IamportResponseException, IOException {
        // Given
        PaymentCallbackRequestDto request = new PaymentCallbackRequestDto();
        IamportResponse<Payment> iamportResponse = mock(IamportResponse.class);
        Payment payment = mock(Payment.class);
        MarketPurchaseRequest marketPurchaseRequest = mock(MarketPurchaseRequest.class);
        Pay entityPay = mock(Pay.class);

        given(iamportClient.paymentByImpUid(anyString())).willReturn(iamportResponse);
        given(iamportResponse.getResponse()).willReturn(payment);
        given(payment.getStatus()).willReturn("ready");
        given(marketPurchaseRequestRepository.findById(anyLong())).willReturn(Optional.of(marketPurchaseRequest));
        given(marketPurchaseRequest.getPay()).willReturn(entityPay);

        // When & Then
        assertThrows(MarkethingException.class, () -> paymentService.paymentByCallback(request));
    }

    @Test
    void testPaymentByCallback_OrderNotExist() throws IamportResponseException, IOException {
        // Given
        PaymentCallbackRequestDto request = new PaymentCallbackRequestDto();
        given(marketPurchaseRequestRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThrows(MarkethingException.class, () -> paymentService.paymentByCallback(request));
    }

    @Test
    void testCancelPayment_PaymentNotFound() {
        // Given
        Long paymentId = 1L;
        CancelPaymentRequestDto request = new CancelPaymentRequestDto();

        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(MarkethingException.class, () -> {
            paymentService.cancelPayment(paymentId, request);
        });

        // Then
        assertNotNull(exception);
        verify(paymentRepository, never()).save(any(Pay.class));
    }

    @Test
    void testCancelPayment_IamportError() throws IamportResponseException, IOException {
        // Given
        Long paymentId = 1L;
        CancelPaymentRequestDto request = new CancelPaymentRequestDto();

        Pay mockPay = mock(Pay.class);

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(mockPay));
        when(iamportClient.cancelPaymentByImpUid(any(CancelData.class))).thenThrow(IamportResponseException.class);

        // When
        Exception exception = assertThrows(MarkethingException.class, () ->  {
            paymentService.cancelPayment(paymentId, request);
        });

        // Then
        assertNotNull(exception);
        verify(paymentRepository, never()).save(any(Pay.class));
    }

    @Test
    void getPayList() {
        // Given
        Long userId = 1L;
        String email = "abc@gmail.com";

        // Create some Pay entities
        Pay pay1 = Pay.builder()
                .payMethod("CARD")
                .status(PaymentStatus.OK)
                .amount(1000)
                .createdAt(LocalDateTime.now())
                .build();
        Pay pay2 = Pay.builder()
                .payMethod("CASH")
                .status(PaymentStatus.CANCEL)
                .amount(2000)
                .createdAt(LocalDateTime.now())
                .build();
        List<Pay> pays = Arrays.asList(pay1, pay2);

        // Mock the repository method to return the Pay entities
        given(paymentRepository.findBySiteUser(siteUserRepository.findById(userId))).willReturn(pays);

        // When
        List<PayResponseDto> result = paymentService.listPayment(email);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).getPayMethod()).isEqualTo("CARD");
        assertThat(result.get(0).getStatus()).isEqualTo(PaymentStatus.OK);
        assertThat(result.get(0).getAmount()).isEqualTo(1000);

        assertThat(result.get(1).getPayMethod()).isEqualTo("CASH");
        assertThat(result.get(1).getStatus()).isEqualTo(PaymentStatus.CANCEL);
        assertThat(result.get(1).getAmount()).isEqualTo(2000);

        verify(paymentRepository).findBySiteUser(siteUserRepository.findById(userId));
    }

    @Test
    public void testDetailPayment_Success() {
        // Mock Data
        Long paymentId = 1L;
        String userEmail = "test@example.com";

        Pay mockPay = Pay.builder()
                .id(1L)
                .amount(1000)
                .status(PaymentStatus.OK)
                .payMethod("card")
                .build();

        SiteUser mockUser = new SiteUser();

        when(siteUserRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(mockPay));

        // Call Service Method
        Optional<PayDetailDto> result = paymentService.detailPayment(paymentId, userEmail);

        // Assertions
        assertTrue(result.isPresent());
        assertEquals("card", result.get().getPayMethod());
        assertEquals(PaymentStatus.OK, result.get().getStatus());
        assertEquals(1000, result.get().getAmount());

        // Verify Repository Interactions
        verify(siteUserRepository, times(1)).findByEmail(anyString());
        verify(paymentRepository, times(1)).findById(anyLong());
    }
}