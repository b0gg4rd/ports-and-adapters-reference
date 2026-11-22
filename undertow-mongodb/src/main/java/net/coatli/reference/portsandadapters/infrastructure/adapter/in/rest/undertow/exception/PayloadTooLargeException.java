package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.exception;

public class PayloadTooLargeException extends RuntimeException {

  public PayloadTooLargeException(final String message) {

    super(message);

  }

}
