package com.example.demo.payment.controller;

import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.payment.controller.api.PaymentApiController;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.demo.payment.controller.view.PaymentController;
import com.example.demo.payment.dto.*;
import com.example.demo.payment.entity.Pay;
import com.example.demo.payment.repository.PaymentRepository;
import com.example.demo.payment.service.PaymentService;
import com.example.demo.type.PaymentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.mockito.Mock;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class PayApiControllerTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private PaymentApiController paymentController;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    public void testValidationPayment() throws Exception {
        // Given
        PaymentCallbackRequestDto request = new PaymentCallbackRequestDto();

        IamportResponse<Payment> response = new IamportResponse<>();
        Mockito.when(paymentService.paymentByCallback(Mockito.any(PaymentCallbackRequestDto.class))).thenReturn(response);

        // When
        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    public void testCancelPayment() throws Exception {
        // Given
        Long paymentId = 1234L;
        CancelPaymentRequestDto request = new CancelPaymentRequestDto();

        IamportResponse<Payment> response = new IamportResponse<>();
        Mockito.when(paymentService.cancelPayment(Mockito.eq(paymentId), Mockito.any(CancelPaymentRequestDto.class))).thenReturn(response);

        // When
        mockMvc.perform(post("/api/payments/{paymentId}/cancel", paymentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    public void testValidationPaymentWithBadRequest() throws Exception {
        // Given
        String invalidJson = "{ invalid json }";

        // When
        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void listTest() throws Exception {
        // Given
        Long userId = 1L;
        String email = "abc@gmail.com";

        PayResponseDto dto1 = new PayResponseDto("CARD", PaymentStatus.OK, 1000, LocalDateTime.now());
        PayResponseDto dto2 = new PayResponseDto("CASH", PaymentStatus.CANCEL, 2000, LocalDateTime.now());
        List<PayResponseDto> expectedDtos = Arrays.asList(dto1, dto2);

        when(paymentService.listPayment(email)).thenReturn(expectedDtos);

        // When
        List<PayResponseDto> result = paymentService.listPayment(email);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);

        assertThat(result.get(0))
                .extracting("payMethod", "status", "amount")
                .containsExactly("CARD", PaymentStatus.OK, 1000);

        assertThat(result.get(1))
                .extracting("payMethod", "status", "amount")
                .containsExactly("CASH", PaymentStatus.CANCEL, 2000);
    }

    @Test
    @DisplayName("payment detail 테스트")
    void testPaymentDetailFound() throws Exception {
        // Given
        Long paymentId = 1L;
        String username = "testUser";
        PayDetailDto dto = PayDetailDto.builder()
                .payMethod("CARD")
                .cardNumber("1234567890")
                .amount(1000)
                .paidAt(LocalDate.now())
                .status(PaymentStatus.OK)
                .build();

        given(paymentService.detailPayment(paymentId, username)).willReturn(Optional.of(dto));

        // When
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/payments/list/{paymentId}", paymentId)
                .contentType(MediaType.APPLICATION_JSON)
                .with(request -> {
                    request.setRemoteUser(username); // Simulate authenticated user
                    return request;
                }));

        // Then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.payMethod").value(dto.getPayMethod()))
                .andExpect(jsonPath("$.cardNumber").value(dto.getCardNumber()))
                .andExpect(jsonPath("$.amount").value(dto.getAmount()))
                .andExpect(jsonPath("$.paidAt").value(dto.getPaidAt().toString()))
                .andExpect(jsonPath("$.status").value(dto.getStatus().toString()))
                .andExpect(jsonPath("$.failReason").doesNotExist());
    }
}
