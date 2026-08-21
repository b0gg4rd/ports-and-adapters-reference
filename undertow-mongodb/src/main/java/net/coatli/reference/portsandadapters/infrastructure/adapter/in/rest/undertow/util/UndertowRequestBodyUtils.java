package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util;

import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import io.undertow.server.HttpServerExchange;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UndertowRequestBodyUtils {

  private static final int EMPTY_BODY_REQUEST_LENGTH = 0;

  public static <T> T deserializeBody(final HttpServerExchange        httpServerExchange,
                                      final JsonTransformationPortOut jsonTransformationPortOut,
                                      final Class<T>                  clazz) {

    if (EMPTY_BODY_REQUEST_LENGTH == httpServerExchange.getRequestContentLength()) {

      throw new IllegalArgumentException("The request body is empty");

    }

    try {

      return jsonTransformationPortOut.fromJson(
        httpServerExchange.getInputStream().readAllBytes(),
        clazz);

    } catch (final Exception exception) {

      throw new IllegalArgumentException("Error processing the body", exception);

    }

  }

}
