package net.coatli.reference.portsandadapters.application.port.in.exception;

public class PaginationInvalidException extends RuntimeException {

  public PaginationInvalidException(String message) {
    super(message);
  }

  public PaginationInvalidException(String message, Throwable cause) {
    super(message, cause);
  }

}
