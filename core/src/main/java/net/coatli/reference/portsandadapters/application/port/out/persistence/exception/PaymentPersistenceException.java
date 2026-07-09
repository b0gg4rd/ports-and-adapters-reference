package net.coatli.reference.portsandadapters.application.port.out.persistence.exception;

public class PaymentPersistenceException extends RuntimeException {

  public PaymentPersistenceException(final String message) {
    super(message);
  }

  public PaymentPersistenceException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
