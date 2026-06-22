package net.coatli.reference.portsandadapters.application.port.in.exception;

public class PaymentStateException extends RuntimeException {

  public PaymentStateException(String message) {
    super(message);
  }

  public PaymentStateException(String message, Throwable cause) {
    super(message, cause);
  }

}
