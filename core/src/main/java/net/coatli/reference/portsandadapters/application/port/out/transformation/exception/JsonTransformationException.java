package net.coatli.reference.portsandadapters.application.port.out.transformation.exception;

public class JsonTransformationException extends RuntimeException {

  public JsonTransformationException(String message) {
    super(message);
  }

  public JsonTransformationException(String message, Throwable cause) {
    super(message, cause);
  }

}
