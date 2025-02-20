package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.YearMonth;

public class PostPaymentRequest implements Serializable {

  @JsonProperty("card_number")
  @NotNull
  @Pattern(regexp = "\\d{14,19}", message = "Card number must be 14-19 digits long and contain only numbers")
  private String cardNumber;

  @JsonIgnore 
  private int cardNumberLastFour;

  @JsonIgnore 
  private boolean expiryDateValid;

  @JsonIgnore 
  @Min(1) @Max(12)
  private int expiryMonth;
  
  @JsonIgnore 
  @Min(2024) 
  private int expiryYear;

  @JsonProperty("expiry_date")
  private String expiryDate;

  @NotNull
  @Size(min = 3, max = 3, message = "Currency must be exactly 3 characters")
  private String currency;

  @Min(1)
  private int amount; 

  @NotNull
  @Min(100) @Max(999) 
  private int cvv;

  public String getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(String cardNumber) {
    this.cardNumber = cardNumber;
    this.cardNumberLastFour = extractLastFourDigits(cardNumber);
  }

  public int getCardNumberLastFour() {
    return cardNumberLastFour;
  }

  private int extractLastFourDigits(String cardNumber) {
    if (cardNumber != null && cardNumber.length() >= 4) {
      return Integer.parseInt(cardNumber.substring(cardNumber.length() - 4));
    }
    return 0; 
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

  public String getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(String expiryDate) {
    this.expiryDate = expiryDate;
    extractExpiryMonthYear(expiryDate);
  }

  private void extractExpiryMonthYear(String expiryDate) {
    if (expiryDate != null && expiryDate.matches("\\d{2}/\\d{4}")) {
      String[] parts = expiryDate.split("/");
      this.expiryMonth = Integer.parseInt(parts[0]);
      this.expiryYear = Integer.parseInt(parts[1]);
    }
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

  public int getCvv() {
    return cvv;
  }

  public void setCvv(int cvv) {
    this.cvv = cvv;
  }

  @JsonIgnore 
  public boolean isExpiryDateValid() {
    YearMonth currentYearMonth = YearMonth.now();
    YearMonth cardExpiry = YearMonth.of(expiryYear, expiryMonth);
    return cardExpiry.isAfter(currentYearMonth);
  }

  @Override
  public String toString() {
    return "PostPaymentRequest{" +
        "cardNumber='" + cardNumber + '\'' +
        ", expiryDate='" + expiryDate + '\'' +
        ", currency='" + currency + '\'' +
        ", amount=" + amount +
        ", cvv=" + cvv +
        '}';
  }
}
