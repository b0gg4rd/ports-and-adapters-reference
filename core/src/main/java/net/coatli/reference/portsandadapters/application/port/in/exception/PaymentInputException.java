package net.coatli.reference.portsandadapters.application.port.in.exception;

public class PaymentInputException extends RuntimeException {

  public PaymentInputException(String message) {
    super(message);
  }

  public PaymentInputException(String message, Throwable cause) {
    super(message, cause);
  }

}
