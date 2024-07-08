package com.example.demo.payment.controller.api;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.demo.exception.MarkethingException;
import com.example.demo.payment.dto.PaymentCallbackRequest;
import com.example.demo.payment.service.PaymentService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.example.demo.exception.type.ErrorCode.ORDER_NOT_EXIST;
import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentApiControllerTest {

    @InjectMocks
    private PaymentApiController paymentApiController;

    @Mock
    private PaymentService paymentService;

    private ListAppender<ILoggingEvent> logAppender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Logger logger = (Logger) LoggerFactory.getLogger(PaymentApiController.class);
        logAppender = new ListAppender<>();
        logAppender.start();
        logger.addAppender(logAppender);
    }

    @Test
    void testValidationPayment_Success() throws Exception {
        // Given
        PaymentCallbackRequest request = new PaymentCallbackRequest("order123", "imp123");
        IamportResponse<Payment> mockResponse = mock(IamportResponse.class);
        Payment mockPayment = mock(Payment.class);

        given(paymentService.paymentByCallback(any(PaymentCallbackRequest.class))).willReturn(mockResponse);
        given(mockResponse.getResponse()).willReturn(mockPayment);
        given(mockPayment.toString()).willReturn("MockPaymentString");

        // When
        paymentApiController.validationPayment(request);

        // Then
        then(paymentService).should().paymentByCallback(request);

        // Log verification
        List<ILoggingEvent> logsList = logAppender.list;
        assertTrue(logsList.stream()
                .anyMatch(event -> event.getFormattedMessage().contains("결제 응답=MockPaymentString")));
    }

    @Test
    void testValidationPayment_PaymentServiceThrowsException() {
        // Given
        PaymentCallbackRequest request = new PaymentCallbackRequest("order123", "imp123");
        given(paymentService.paymentByCallback(any(PaymentCallbackRequest.class)))
                .willThrow(new MarkethingException(ORDER_NOT_EXIST));

        // When & Then
        assertThrows(MarkethingException.class, () -> paymentApiController.validationPayment(request));
        then(paymentService).should().paymentByCallback(request);

        // Log verification
        List<ILoggingEvent> logsList = logAppender.list;
        assertTrue(logsList.isEmpty());
    }
}