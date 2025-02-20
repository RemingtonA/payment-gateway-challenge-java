package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.model.GetPaymentResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentGatewayServiceTest {

    private PaymentGatewayService paymentGatewayService;
    private PaymentsRepository paymentsRepository;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        paymentsRepository = mock(PaymentsRepository.class);
        restTemplate = mock(RestTemplate.class);
        paymentGatewayService = new PaymentGatewayService(paymentsRepository, restTemplate);
    }

    @Test
    void processPayment_ShouldReturnAuthorizedResponse_WhenValidRequest() {
        PostPaymentRequest request = new PostPaymentRequest();
        request.setCardNumber("1234567890123456");
        request.setExpiryMonth(12);
        request.setExpiryYear(2025);
        request.setCurrency("USD");
        request.setAmount(100);
        request.setCvv(123);

        PostPaymentResponse response = paymentGatewayService.processPayment(request);

        assertNotNull(response.getId());
        assertEquals(PaymentStatus.AUTHORIZED, response.getStatus());
        assertEquals("3456", response.getCardNumberLastFour());
        assertEquals(12, response.getExpiryMonth());
        assertEquals(2025, response.getExpiryYear());
        assertEquals("USD", response.getCurrency());
        assertEquals(100, response.getAmount());

        verify(paymentsRepository).add(any(PostPaymentResponse.class));
    }

    void getPaymentById_ShouldReturnCorrectPayment_WhenPaymentExists() {
        UUID paymentId = UUID.randomUUID();
        
        PostPaymentResponse storedPayment = new PostPaymentResponse(
            paymentId,
            PaymentStatus.AUTHORIZED,
            "1234",
            12,
            2025,
            "USD",
            100
        );

        when(paymentsRepository.get(paymentId)).thenReturn(Optional.of(storedPayment));

        GetPaymentResponse response = paymentGatewayService.getPaymentById(paymentId);

        assertNotNull(response);
        assertEquals(paymentId, response.getId());
        assertEquals(PaymentStatus.AUTHORIZED, response.getStatus());
        assertEquals("1234", response.getCardNumberLastFour());
        assertEquals(12, response.getExpiryMonth());
        assertEquals(2025, response.getExpiryYear());
        assertEquals("USD", response.getCurrency());
        assertEquals(100, response.getAmount());
    }
    @Test
    void getPaymentById_ShouldThrowException_WhenPaymentDoesNotExist() {
        UUID paymentId = UUID.randomUUID();
        when(paymentsRepository.get(paymentId)).thenReturn(Optional.empty());

        EventProcessingException exception = assertThrows(
            EventProcessingException.class,
            () -> paymentGatewayService.getPaymentById(paymentId)
        );

        assertEquals("Invalid payment ID", exception.getMessage());
    }
}
