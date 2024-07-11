package com.example.demo.payment.controller;

import com.example.demo.payment.dto.CancelPaymentRequestDto;
import com.example.demo.payment.dto.PayResponseDto;
import com.example.demo.payment.dto.PaymentCallbackRequestDto;
import com.example.demo.payment.dto.PaymentListRequestDto;
import com.example.demo.payment.repository.PaymentRepository;
import com.example.demo.payment.service.PaymentService;
import com.example.demo.type.PaymentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.BDDMockito.*;
import org.mockito.Mock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
    public void testCancelPaymentWithBadRequest() throws Exception {
        // Given
        String paymentId = "testPaymentId";
        String invalidJson = "{ invalid json }";

        // When
        mockMvc.perform(post("/api/payments/{paymentId}/cancel", paymentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void listTest() throws Exception {
        // Given
        Long userId = 1L;
        PaymentListRequestDto requestDto = new PaymentListRequestDto(userId);

        PayResponseDto dto1 = new PayResponseDto("CARD", PaymentStatus.OK, 1000, LocalDateTime.now());
        PayResponseDto dto2 = new PayResponseDto("CASH", PaymentStatus.CANCEL, 2000, LocalDateTime.now());
        List<PayResponseDto> expectedDtos = Arrays.asList(dto1, dto2);

        when(paymentService.listPayment(any(PaymentListRequestDto.class))).thenReturn(expectedDtos);

        // When
        List<PayResponseDto> result = paymentService.listPayment(requestDto);

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
}
