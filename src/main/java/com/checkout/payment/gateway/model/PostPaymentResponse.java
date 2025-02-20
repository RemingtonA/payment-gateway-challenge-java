package com.checkout.payment.gateway.model;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public class PostPaymentResponse {

  @JsonProperty("id")
  @Schema(description = "Unique payment identifier", example = "550e8400-e29b-41d4-a716-446655440000")
  private UUID id;

  @JsonProperty("status")
  @Schema(description = "Payment status", example = "AUTHORIZED")
  private PaymentStatus status;

  @JsonProperty("card_number_last_four")
  @Schema(description = "Last four digits of the card number", example = "8877")
  private String cardNumberLastFour;

  @JsonProperty("expiry_month")
  @Schema(description = "Expiry month (1-12)", example = "4")
  private int expiryMonth;

  @JsonProperty("expiry_year")
  @Schema(description = "Expiry year (must be in the future)", example = "2025")
  private int expiryYear;

  @Schema(description = "Currency (ISO 4217 code)", example = "GBP")
  private String currency;

  @Schema(description = "Amount in minor currency unit (e.g., 100 = £1.00)", example = "100")
  private int amount;

  public PostPaymentResponse() {}

  public PostPaymentResponse(UUID id, PaymentStatus status, String cardNumberLastFour, int expiryMonth, int expiryYear, String currency, int amount) {
    this.id = id;
    this.status = status;
    this.cardNumberLastFour = String.valueOf(cardNumberLastFour);
    this.expiryMonth = expiryMonth;
    this.expiryYear = expiryYear;
    this.currency = currency;
    this.amount = amount;
  }

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
    return "PostPaymentResponse{" +
        "id=" + id +
        ", status=" + status +
        ", cardNumberLastFour=" + cardNumberLastFour +
        ", expiryMonth=" + expiryMonth +
        ", expiryYear=" + expiryYear +
        ", currency='" + currency + '\'' +
        ", amount=" + amount +
        '}';
  }
}
