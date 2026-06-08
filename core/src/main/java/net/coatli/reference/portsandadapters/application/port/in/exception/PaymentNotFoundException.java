package net.coatli.reference.portsandadapters.application.port.in.exception;

public class PaymentNotFoundException extends RuntimeException {

  public PaymentNotFoundException(String message) {
    super(message);
  }

}
