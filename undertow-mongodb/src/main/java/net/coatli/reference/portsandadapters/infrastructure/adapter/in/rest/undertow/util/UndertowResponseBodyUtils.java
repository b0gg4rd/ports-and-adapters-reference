package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util;

import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UndertowResponseBodyUtils {

  public static void createOkResponse(final HttpServerExchange httpServerExchange,
                                      final LoggingPortOut     loggingPortOut,
                                      final Class<?>           caller,
                                      final String             result) {

    loggingPortOut.info(
      caller,
      "[rest.response.ok] output: '{} {}'",
      StatusCodes.OK,
      result);

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

    UndertowHeaderUtils.putResponseHeader(
      httpServerExchange,
      headerName,
      headerValue);

    httpServerExchange
      .setStatusCode(StatusCodes.CREATED)
      .endExchange();

  }

  public static void createUnprocessableBodyResponse(final HttpServerExchange httpServerExchange,
                                                     final String             message,
                                                     final LoggingPortOut     loggingPortOut,
                                                     final Class<?>           caller,
                                                     final Exception          exception) {

    loggingPortOut.error(
      caller,
      "[rest.response.unprocessable] output: '{} {}'",
      StatusCodes.UNPROCESSABLE_ENTITY,
      message,
      exception);

    UndertowHeaderUtils.putResponseHeader(
      httpServerExchange,
      Headers.CONTENT_TYPE,
      UndertowHeaderUtils.TEXT_PLAIN_UTF8);

    httpServerExchange
      .setStatusCode(StatusCodes.UNPROCESSABLE_ENTITY)
      .endExchange();

  }

  public static void createBadRequestResponse(final HttpServerExchange httpServerExchange,
                                              final LoggingPortOut     loggingPortOut,
                                              final Class<?>           caller,
                                              final Throwable          exception) {

    loggingPortOut.error(
      caller,
      "[rest.response.bad-request] output: '{} {}'",
      StatusCodes.BAD_REQUEST,
      exception.toString(),
      exception);

    UndertowHeaderUtils.putResponseHeader(
      httpServerExchange,
      Headers.CONTENT_TYPE,
      UndertowHeaderUtils.TEXT_PLAIN_UTF8);

    httpServerExchange
      .setStatusCode(StatusCodes.BAD_REQUEST)
      .endExchange();

  }

  public static void createNotFoundResponse(final HttpServerExchange httpServerExchange,
                                            final LoggingPortOut     loggingPortOut,
                                            final Class<?>           caller,
                                            final Throwable          exception) {

    loggingPortOut.error(
      caller,
      "[rest.response.not-found] output: '{} {}'",
      StatusCodes.NOT_FOUND,
      exception.toString());

    UndertowHeaderUtils.putResponseHeader(
      httpServerExchange,
      Headers.CONTENT_TYPE,
      UndertowHeaderUtils.TEXT_PLAIN_UTF8);

    httpServerExchange
      .setStatusCode(StatusCodes.NOT_FOUND)
      .endExchange();

  }

  public static void createInternalServerErrorResponse(final HttpServerExchange httpServerExchange,
                                                       final LoggingPortOut     loggingPortOut,
                                                       final Class<?>           caller,
                                                       final Throwable          exception) {

    loggingPortOut.error(
      caller,
      "[rest.response.internal-server-error] output: '{} {}'",
      StatusCodes.INTERNAL_SERVER_ERROR,
      exception.toString(),
      exception);

    UndertowHeaderUtils.putResponseHeader(
      httpServerExchange,
      Headers.CONTENT_TYPE,
      UndertowHeaderUtils.TEXT_PLAIN_UTF8);

    httpServerExchange
      .setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR)
      .endExchange();

  }

  public static void createNoContentResponse(HttpServerExchange   httpServerExchange,
                                             final LoggingPortOut loggingPortOut,
                                             final Class<?>       caller) {

    loggingPortOut.info(
      caller,
      "[rest.response.no-content] output: '{} {}'",
      StatusCodes.NO_CONTENT,
      StatusCodes.NO_CONTENT_STRING);

    UndertowHeaderUtils.putResponseHeader(
      httpServerExchange,
      Headers.CONTENT_TYPE,
      UndertowHeaderUtils.TEXT_PLAIN_UTF8);

    httpServerExchange
      .setStatusCode(StatusCodes.NO_CONTENT)
      .endExchange();

  }

  public static void createConflictResponse(final HttpServerExchange httpServerExchange,
                                            final LoggingPortOut     loggingPortOut,
                                            final Class<?>           caller,
                                            final Throwable          exception) {

    loggingPortOut.error(
      caller,
      "[rest.response.conflict] output: '{} {}'",
      StatusCodes.CONFLICT,
      exception.toString(),
      exception);

    UndertowHeaderUtils.putResponseHeader(
      httpServerExchange,
      Headers.CONTENT_TYPE,
      UndertowHeaderUtils.TEXT_PLAIN_UTF8);

    httpServerExchange
      .setStatusCode(StatusCodes.CONFLICT)
      .endExchange();

  }

}
