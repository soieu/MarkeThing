package com.example.demo.payment.controller.api;

import com.example.demo.payment.dto.CancelPaymentRequest;
import com.example.demo.payment.dto.PaymentCallbackRequest;
import com.example.demo.payment.service.PaymentService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testValidationPayment() throws Exception {
        // Given
        PaymentCallbackRequest request = new PaymentCallbackRequest("123" , "!23");

        IamportResponse<Payment> response = new IamportResponse<>();
        Mockito.when(paymentService.paymentByCallback(Mockito.any(PaymentCallbackRequest.class))).thenReturn(response);

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
        String paymentId = "testPaymentId";
        CancelPaymentRequest request = new CancelPaymentRequest();
        // request에 필요한 필드를 설정하세요

        IamportResponse<Payment> response = new IamportResponse<>();
        Mockito.when(paymentService.cancelPayment(Mockito.eq(paymentId), Mockito.any(CancelPaymentRequest.class))).thenReturn(response);

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
}
