package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util;

import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.exception.PayloadTooLargeException;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.exception.UnsupportedMediaTypeException;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RequestTooBigException;
import io.undertow.util.Headers;
import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class UndertowRequestBodyUtils {

  private static final int EMPTY_BODY_REQUEST_LENGTH = 0;

  private static final char MEDIA_TYPE_PARAMETERS_SEPARATOR = ';';

  private static final String APPLICATION_JSON = "application/json";

  public static <T> T deserializeBody(final HttpServerExchange        httpServerExchange,
                                      final JsonTransformationPortOut jsonTransformationPortOut,
                                      final Class<T>                  clazz) {

    final var contentLength = httpServerExchange.getRequestContentLength();

    if (EMPTY_BODY_REQUEST_LENGTH == contentLength) {

      throw new IllegalArgumentException("The request body is empty");

    }

    if (contentLength > UndertowAppUtils.MAX_BODY_SIZE) {

      throw new PayloadTooLargeException(payloadTooLargeMessage());

    }

    validateContentType(httpServerExchange);

    final var body = readBody(httpServerExchange);

    if (EMPTY_BODY_REQUEST_LENGTH == body.length) {

      throw new IllegalArgumentException("The request body is empty");

    }

    try {

      return jsonTransformationPortOut.fromJson(body, clazz);

    } catch (final Exception exception) {

      throw new IllegalArgumentException("Error processing the body", exception);

    }

  }

  private static void validateContentType(final HttpServerExchange httpServerExchange) {

    final var contentType = UndertowHeaderUtils.getCombinedHeaderValue(
      httpServerExchange,
      Headers.CONTENT_TYPE_STRING);

    if (null == contentType || !APPLICATION_JSON.equalsIgnoreCase(mediaType(contentType))) {

      throw new UnsupportedMediaTypeException(
        String.format("The Content-Type must be '%s'", APPLICATION_JSON));

    }

  }

  private static String mediaType(final String contentType) {

    final var separatorIndex = contentType.indexOf(MEDIA_TYPE_PARAMETERS_SEPARATOR);

    return
      (-1 == separatorIndex
        ? contentType
        : contentType.substring(0, separatorIndex))
        .trim();

  }

  private static byte[] readBody(final HttpServerExchange httpServerExchange) {

    try {

      final var body = httpServerExchange.getInputStream().readNBytes((int) UndertowAppUtils.MAX_BODY_SIZE + 1);

      if (body.length > UndertowAppUtils.MAX_BODY_SIZE) {

        throw new PayloadTooLargeException(payloadTooLargeMessage());

      }

      return body;

    } catch (final RequestTooBigException exception) {

      throw new PayloadTooLargeException(payloadTooLargeMessage());

    } catch (final IOException exception) {

      throw new IllegalArgumentException("Error reading the body", exception);

    }

  }

  private static String payloadTooLargeMessage() {

    return String.format("The request body exceeds %d bytes", UndertowAppUtils.MAX_BODY_SIZE);

  }

}
