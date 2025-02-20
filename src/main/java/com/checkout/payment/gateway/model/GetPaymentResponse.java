package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public class GetPaymentResponse {

  @Schema(description = "Unique payment identifier", example = "550e8400-e29b-41d4-a716-446655440000")
  @JsonProperty("id")
  private UUID id;

  @Schema(description = "Payment status", example = "AUTHORIZED")
  @JsonProperty("status")
  private PaymentStatus status;

  @Schema(description = "Last four digits of the card number", example = "8877")
  @JsonProperty("card_number_last_four")
  private String cardNumberLastFour; 

  @Schema(description = "Expiry month (1-12)", example = "4")
  @JsonProperty("expiry_month")
  private int expiryMonth;

  @Schema(description = "Expiry year (must be in the future)", example = "2025")
  @JsonProperty("expiry_year")
  private int expiryYear;

  @Schema(description = "Currency (ISO 4217 code)", example = "GBP")
  @JsonProperty("currency")
  private String currency;

  @Schema(description = "Amount in minor currency unit (e.g., 100 = £1.00)", example = "100")
  @JsonProperty("amount")
  private int amount;


  public GetPaymentResponse(UUID id, PaymentStatus status, String cardNumberLastFour, 
                            int expiryMonth, int expiryYear, String currency, int amount) {
    this.id = id;
    this.status = status;
    this.cardNumberLastFour = cardNumberLastFour;
    this.expiryMonth = expiryMonth;
    this.expiryYear = expiryYear;
    this.currency = currency;
    this.amount = amount;
  }


  public GetPaymentResponse() {}

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public PaymentStatus getStatus() {
    return status;
  }

  public void setStatus(PaymentStatus status) {
    this.status = status;
  }

  public String getCardNumberLastFour() {
    return cardNumberLastFour;
  }

  public void setCardNumberLastFour(String cardNumberLastFour) {
    this.cardNumberLastFour = cardNumberLastFour;
  }

  public int getExpiryMonth() {
    return expiryMonth;
  }

  public void setExpiryMonth(int expiryMonth) {
    this.expiryMonth = expiryMonth;
  }

  public int getExpiryYear() {
    return expiryYear;
  }

  public void setExpiryYear(int expiryYear) {
    this.expiryYear = expiryYear;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "GetPaymentResponse{" +
        "id=" + id +
        ", status=" + status +
        ", cardNumberLastFour='" + cardNumberLastFour + '\'' + 
        ", expiryMonth=" + expiryMonth +
        ", expiryYear=" + expiryYear +
        ", currency='" + currency + '\'' +
        ", amount=" + amount +
        '}';
  }
}
