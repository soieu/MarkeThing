package com.example.demo.payment.controller;

import com.example.demo.payment.dto.CancelPaymentRequestDto;
import com.example.demo.payment.dto.PayResponseDto;
import com.example.demo.payment.dto.PaymentCallbackRequestDto;
import com.example.demo.payment.dto.PaymentListRequestDto;
import com.example.demo.payment.service.PaymentService;
import com.example.demo.type.PaymentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PayApiControllerTest {

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
        Mockito.when(paymentService.paymentByCallback(any(PaymentCallbackRequestDto.class))).thenReturn(response);

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
        Mockito.when(paymentService.cancelPayment(Mockito.eq(paymentId), any(CancelPaymentRequestDto.class))).thenReturn(response);

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
    void getPaymentList_shouldReturnListOfPayments() throws Exception {
        // Given
        Long userId = 1L;
        PaymentListRequestDto requestDto = new PaymentListRequestDto(userId);

        PayResponseDto dto1 = new PayResponseDto("CARD", PaymentStatus.OK, 1000, LocalDateTime.now());
        PayResponseDto dto2 = new PayResponseDto("CASH", PaymentStatus.CANCEL, 2000, LocalDateTime.now());
        List<PayResponseDto> expectedDtos = Arrays.asList(dto1, dto2);

        given(paymentService.listPayment(requestDto)).willReturn(expectedDtos);

        // When & Then
        mockMvc.perform(post("/api/payments/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].payMethod").value("CARD"))
                .andExpect(jsonPath("$[0].status").value("OK"))
                .andExpect(jsonPath("$[0].amount").value(1000))
                .andExpect(jsonPath("$[1].payMethod").value("CASH"))
                .andExpect(jsonPath("$[1].status").value("CANCEL"))
                .andExpect(jsonPath("$[1].amount").value(2000));
    }


}
