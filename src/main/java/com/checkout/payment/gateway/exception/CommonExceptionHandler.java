package com.checkout.payment.gateway.exception;

import com.checkout.payment.gateway.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

  private static final Logger LOG = LoggerFactory.getLogger(CommonExceptionHandler.class);

  /**
   * Handles business logic errors in PaymentGatewayService.
   */
  @ExceptionHandler(EventProcessingException.class)
  public ResponseEntity<ErrorResponse> handleEventProcessingException(EventProcessingException ex) {
    LOG.error("Application error: {}", ex.getMessage(), ex);
    
    HttpStatus status = HttpStatus.BAD_REQUEST;

    if (ex.getMessage().contains("Invalid payment ID")) {
      status = HttpStatus.NOT_FOUND;
    } else if (ex.getMessage().contains("unreachable")) {
      status = HttpStatus.SERVICE_UNAVAILABLE;
    } else if (ex.getMessage().contains("Bank error")) {
      status = HttpStatus.BAD_GATEWAY;
    }

    return new ResponseEntity<>(new ErrorResponse(ex.getMessage()), status);
  }

  /**
   * Handles all unexpected runtime exceptions.
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
    LOG.error("Unexpected error: {}", ex.getMessage(), ex);
    return new ResponseEntity<>(new ErrorResponse("Unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
