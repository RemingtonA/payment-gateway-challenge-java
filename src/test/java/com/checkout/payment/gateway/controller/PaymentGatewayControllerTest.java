package com.checkout.payment.gateway.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import com.checkout.payment.gateway.service.PaymentGatewayService;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentGatewayControllerTest {

  @Autowired
  private MockMvc mvc;
  
  @Autowired
  PaymentsRepository paymentsRepository;
  
  @Autowired
  PaymentGatewayService paymentGatewayService;
  
  private static final ObjectMapper objectMapper = new ObjectMapper();


  @Test
  void getPaymentById_ShouldReturnPaymentDetails() throws Exception {
      PostPaymentResponse payment = new PostPaymentResponse();
      payment.setId(UUID.randomUUID());
      payment.setAmount(100);
      payment.setCurrency("USD");
      payment.setStatus(PaymentStatus.AUTHORIZED);
      payment.setExpiryMonth(12);
      payment.setExpiryYear(2025);
      payment.setCardNumberLastFour("3456"); 
      paymentsRepository.add(payment);

      mvc.perform(MockMvcRequestBuilders.get("/v1/payment/" + payment.getId()))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(payment.getId().toString()))
          .andExpect(jsonPath("$.status").value(payment.getStatus().getName()))
          .andExpect(jsonPath("$.card_number_last_four").value(payment.getCardNumberLastFour()))
          .andExpect(jsonPath("$.expiry_month").value(payment.getExpiryMonth()))
          .andExpect(jsonPath("$.expiry_year").value(payment.getExpiryYear()))
          .andExpect(jsonPath("$.currency").value(payment.getCurrency()))
          .andExpect(jsonPath("$.amount").value(payment.getAmount()));
  }

  @Test
  void getPaymentById_ShouldReturn404_WhenPaymentDoesNotExist() throws Exception {
      mvc.perform(MockMvcRequestBuilders.get("/v1/payment/" + UUID.randomUUID()))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message").value("Invalid payment ID"));
  }
}
