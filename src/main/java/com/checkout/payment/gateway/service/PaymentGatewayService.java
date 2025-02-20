package com.checkout.payment.gateway.service;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.model.GetPaymentResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentGatewayService {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);
  private static final String BANK_SIMULATOR_URL = "http://localhost:8080/payments";

  private final PaymentsRepository paymentsRepository;
  private final RestTemplate restTemplate;

  public PaymentGatewayService(PaymentsRepository paymentsRepository, RestTemplate restTemplate) {
    this.paymentsRepository = paymentsRepository;
    this.restTemplate = restTemplate;
  }

  public PostPaymentResponse processPayment(PostPaymentRequest paymentRequest) {
    validatePaymentRequest(paymentRequest);

    UUID paymentId = UUID.randomUUID();
    PaymentStatus status = callBankSimulator(paymentRequest);

    PostPaymentResponse response = new PostPaymentResponse(
        paymentId,
        status,
        String.valueOf(paymentRequest.getCardNumberLastFour()),
        paymentRequest.getExpiryMonth(),
        paymentRequest.getExpiryYear(),
        paymentRequest.getCurrency(),
        paymentRequest.getAmount()
    );

    paymentsRepository.add(response);
    LOG.info("Payment processed successfully: {}", response);
    return response;
  }

  private PaymentStatus callBankSimulator(PostPaymentRequest paymentRequest) {
    try {
      Map<String, Object> requestBody = new HashMap<>();
      requestBody.put("card_number", paymentRequest.getCardNumber());
      requestBody.put("expiry_date", String.format("%02d/%d", paymentRequest.getExpiryMonth(), paymentRequest.getExpiryYear()));
      requestBody.put("currency", paymentRequest.getCurrency());
      requestBody.put("amount", paymentRequest.getAmount());
      requestBody.put("cvv", paymentRequest.getCvv());

      LOG.info("Sending request to bank simulator: {}", requestBody);

      ResponseEntity<Map> response = restTemplate.postForEntity(BANK_SIMULATOR_URL, requestBody, Map.class);

      Map<String, Object> responseBody = response.getBody();
      boolean authorized = responseBody != null && Boolean.TRUE.equals(responseBody.get("authorized"));

      LOG.info("Received response from bank simulator: {}", responseBody);

      return authorized ? PaymentStatus.AUTHORIZED : PaymentStatus.DECLINED;
    } catch (HttpClientErrorException e) {
      LOG.error("Bank simulator returned an error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());

      if (e.getStatusCode() == HttpStatus.BAD_REQUEST) {
        return PaymentStatus.REJECTED;
      } else if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
        throw new EventProcessingException("Bank service unavailable, please try again later");
      } else {
        return PaymentStatus.REJECTED;
      }
    } catch (ResourceAccessException e) {
      LOG.error("Failed to connect to the bank simulator: {}", e.getMessage());
      throw new EventProcessingException("Bank simulator is unreachable, please try again later");
    } catch (Exception e) {
      LOG.error("Unexpected error occurred: {}", e.getMessage());
      throw new EventProcessingException("Unexpected error occurred, please try again later");
    }
  }

  private void validatePaymentRequest(PostPaymentRequest paymentRequest) {
    if (!paymentRequest.isExpiryDateValid()) {
      throw new EventProcessingException("Invalid payment request: Expiry date must be in the future");
    }
  }

  public GetPaymentResponse getPaymentById(UUID id) {
	    LOG.debug("Fetching payment with ID {}", id);
	    PostPaymentResponse storedPayment = paymentsRepository.get(id)
	        .orElseThrow(() -> new EventProcessingException("Invalid payment ID"));

	    return new GetPaymentResponse(
	        storedPayment.getId(),
	        storedPayment.getStatus(),
	        storedPayment.getCardNumberLastFour(),
	        storedPayment.getExpiryMonth(),
	        storedPayment.getExpiryYear(),
	        storedPayment.getCurrency(),
	        storedPayment.getAmount()
	    );
	  }
}
