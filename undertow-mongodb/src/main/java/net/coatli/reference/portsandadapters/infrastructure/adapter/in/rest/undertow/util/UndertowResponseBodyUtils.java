package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util;

import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.ErrorResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UndertowResponseBodyUtils {

  private static final String UNEXPECTED_ERROR_DETAIL = "An unexpected error occurred";

  public static void createOkResponse(final HttpServerExchange httpServerExchange,
                                      final LoggingPortOut     loggingPortOut,
                                      final Class<?>           caller,
                                      final String             result) {

    loggingPortOut.info(
      caller,
      "[rest.response.ok] output: '{} {}'",
      StatusCodes.OK,
      result);

    UndertowHeaderUtils.noCache(httpServerExchange);

    UndertowHeaderUtils.putResponseHeader(
      httpServerExchange,
      Headers.CONTENT_TYPE,
      UndertowHeaderUtils.APPLICATION_JSON_UTF8);

    httpServerExchange
      .setStatusCode(StatusCodes.OK)
      .getResponseSender()
      .send(result);

  }

  public static void createOkWithHeaderResponse(final HttpServerExchange httpServerExchange,
                                                final LoggingPortOut     loggingPortOut,
                                                final Class<?>           caller,
                                                final String             headerName,
                                                final String             headerValue) {

    loggingPortOut.info(
      caller,
      "[rest.response.ok] output: '{} {} {}'",
      StatusCodes.OK,
      headerName,
      headerValue);

    UndertowHeaderUtils.putResponseHeader(
      httpServerExchange,
      headerName,
      headerValue);

    httpServerExchange
      .setStatusCode(StatusCodes.OK)
      .endExchange();

  }

  public static void createCreatedResponse(final HttpServerExchange httpServerExchange,
                                           final LoggingPortOut     loggingPortOut,
                                           final Class<?>           caller,
                                           final String             headerName,
                                           final String             headerValue) {

    loggingPortOut.info(
      caller,
      "[rest.response.created] output: '{} {} {}'",
      StatusCodes.CREATED,
      headerName,
      headerValue);

    UndertowHeaderUtils.noCache(httpServerExchange);

    UndertowHeaderUtils.putResponseHeader(
      httpServerExchange,
      headerName,
      headerValue);

    httpServerExchange
      .setStatusCode(StatusCodes.CREATED)
      .endExchange();

  }

  public static void createNoContentResponse(final HttpServerExchange httpServerExchange,
                                             final LoggingPortOut     loggingPortOut,
                                             final Class<?>           caller) {

    loggingPortOut.info(
      caller,
      "[rest.response.no-content] output: '{} {}'",
      StatusCodes.NO_CONTENT,
      StatusCodes.NO_CONTENT_STRING);

    httpServerExchange
      .setStatusCode(StatusCodes.NO_CONTENT)
      .endExchange();

  }

  public static void createBadRequestResponse(final HttpServerExchange        httpServerExchange,
                                              final JsonTransformationPortOut jsonTransformationPortOut,
                                              final LoggingPortOut            loggingPortOut,
                                              final Class<?>                  caller,
                                              final Throwable                 exception) {

    createErrorResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      StatusCodes.BAD_REQUEST,
      "bad-request",
      exception.getMessage(),
      exception);

  }

  public static void createUnprocessableEntityResponse(final HttpServerExchange        httpServerExchange,
                                                       final JsonTransformationPortOut jsonTransformationPortOut,
                                                       final LoggingPortOut            loggingPortOut,
                                                       final Class<?>                  caller,
                                                       final Throwable                 exception) {

    createErrorResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      StatusCodes.UNPROCESSABLE_ENTITY,
      "unprocessable-entity",
      exception.getMessage(),
      exception);

  }

  public static void createNotFoundResponse(final HttpServerExchange        httpServerExchange,
                                            final JsonTransformationPortOut jsonTransformationPortOut,
                                            final LoggingPortOut            loggingPortOut,
                                            final Class<?>                  caller,
                                            final Throwable                 exception) {

    createErrorResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      StatusCodes.NOT_FOUND,
      "not-found",
      exception.getMessage(),
      exception);

  }

  public static void createConflictResponse(final HttpServerExchange        httpServerExchange,
                                            final JsonTransformationPortOut jsonTransformationPortOut,
                                            final LoggingPortOut            loggingPortOut,
                                            final Class<?>                  caller,
                                            final Throwable                 exception) {

    createErrorResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      StatusCodes.CONFLICT,
      "conflict",
      exception.getMessage(),
      exception);

  }

  public static void createInternalServerErrorResponse(final HttpServerExchange        httpServerExchange,
                                                       final JsonTransformationPortOut jsonTransformationPortOut,
                                                       final LoggingPortOut            loggingPortOut,
                                                       final Class<?>                  caller,
                                                       final Throwable                 exception) {

    createErrorResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      StatusCodes.INTERNAL_SERVER_ERROR,
      "internal-server-error",
      UNEXPECTED_ERROR_DETAIL,
      exception);

  }

  public static void createUnsupportedMediaTypeResponse(final HttpServerExchange        httpServerExchange,
                                                        final JsonTransformationPortOut jsonTransformationPortOut,
                                                        final LoggingPortOut            loggingPortOut,
                                                        final Class<?>                  caller,
                                                        final Throwable                 exception) {

    createErrorResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      StatusCodes.UNSUPPORTED_MEDIA_TYPE,
      "unsupported-media-type",
      exception.getMessage(),
      exception);

  }

  public static void createPayloadTooLargeResponse(final HttpServerExchange        httpServerExchange,
                                                   final JsonTransformationPortOut jsonTransformationPortOut,
                                                   final LoggingPortOut            loggingPortOut,
                                                   final Class<?>                  caller,
                                                   final Throwable                 exception) {

    createErrorResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      StatusCodes.REQUEST_ENTITY_TOO_LARGE,
      "payload-too-large",
      exception.getMessage(),
      exception);

  }

  public static void createMethodNotAllowedResponse(final HttpServerExchange        httpServerExchange,
                                                    final JsonTransformationPortOut jsonTransformationPortOut,
                                                    final LoggingPortOut            loggingPortOut,
                                                    final Class<?>                  caller,
                                                    final String                    allow) {

    if (null != allow) {

      UndertowHeaderUtils.putResponseHeader(
        httpServerExchange,
        Headers.ALLOW,
        allow);

    }

    createErrorResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      StatusCodes.METHOD_NOT_ALLOWED,
      "method-not-allowed",
      String.format("The method '%s' is not allowed for this resource", httpServerExchange.getRequestMethod()),
      null);

  }

  private static void createErrorResponse(final HttpServerExchange        httpServerExchange,
                                          final JsonTransformationPortOut jsonTransformationPortOut,
                                          final LoggingPortOut            loggingPortOut,
                                          final Class<?>                  caller,
                                          final int                       status,
                                          final String                    tag,
                                          final String                    detail,
                                          final Throwable                 exception) {

    final var logFormat = "[rest.response." + tag + "] output: '{} {}'";

    final var logCause = null == exception ? detail : String.valueOf(exception);

    if (status >= StatusCodes.INTERNAL_SERVER_ERROR) {

      loggingPortOut.error(caller, logFormat, status, logCause, exception);

    } else {

      loggingPortOut.error(caller, logFormat, status, logCause);

    }

    final var errorResponse = new ErrorResponse(
      null == detail || detail.isBlank() ? StatusCodes.getReason(status) : detail);

    UndertowHeaderUtils.noCache(httpServerExchange);

    UndertowHeaderUtils.putResponseHeader(
      httpServerExchange,
      Headers.CONTENT_TYPE,
      UndertowHeaderUtils.APPLICATION_JSON_UTF8);

    httpServerExchange
      .setStatusCode(status)
      .getResponseSender()
      .send(jsonTransformationPortOut.toJson(errorResponse));

  }

}
