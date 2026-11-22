package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.config;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaginationInvalidException;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentNotFoundException;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentStateException;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.exception.PayloadTooLargeException;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.exception.UnsupportedMediaTypeException;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.UndertowResponseBodyUtils;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionConfig {

  public static HttpHandler setup(final HttpHandler                routes,
                                  final JsonTransformationPortOut jsonTransformationPortOut,
                                  final LoggingPortOut            loggingPortOut,
                                  final Class<?>                  caller) {

    return Handlers
      .exceptionHandler(routes)
      .addExceptionHandler(
        PaymentInputException.class,
        httpServerExchange -> handlePaymentInputException(httpServerExchange, jsonTransformationPortOut, loggingPortOut, caller))
      .addExceptionHandler(
        PaymentNotFoundException.class,
        httpServerExchange -> handlePaymentNotFoundException(httpServerExchange, jsonTransformationPortOut, loggingPortOut, caller))
      .addExceptionHandler(
        PaymentStateException.class,
        httpServerExchange -> handlePaymentStateException(httpServerExchange, jsonTransformationPortOut, loggingPortOut, caller))
      .addExceptionHandler(
        UnsupportedMediaTypeException.class,
        httpServerExchange -> handleUnsupportedMediaTypeException(httpServerExchange, jsonTransformationPortOut, loggingPortOut, caller))
      .addExceptionHandler(
        PayloadTooLargeException.class,
        httpServerExchange -> handlePayloadTooLargeException(httpServerExchange, jsonTransformationPortOut, loggingPortOut, caller))
      .addExceptionHandler(
        PaginationInvalidException.class,
        httpServerExchange -> handlePaginationInvalidException(httpServerExchange, jsonTransformationPortOut, loggingPortOut, caller))
      .addExceptionHandler(
        IllegalArgumentException.class,
        httpServerExchange -> handleIllegalArgumentException(httpServerExchange, jsonTransformationPortOut, loggingPortOut, caller))
      .addExceptionHandler(
        Exception.class,
        httpServerExchange -> handleGeneralException(httpServerExchange, jsonTransformationPortOut, loggingPortOut, caller));

  }

  private static void handlePaymentInputException(final HttpServerExchange        httpServerExchange,
                                                  final JsonTransformationPortOut jsonTransformationPortOut,
                                                  final LoggingPortOut            loggingPortOut,
                                                  final Class<?>                  caller) {

    UndertowResponseBodyUtils.createUnprocessableEntityResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      (Throwable) httpServerExchange.getAttachment(ExceptionHandler.THROWABLE));

  }

  private static void handlePaymentNotFoundException(final HttpServerExchange        httpServerExchange,
                                                     final JsonTransformationPortOut jsonTransformationPortOut,
                                                     final LoggingPortOut            loggingPortOut,
                                                     final Class<?>                  caller) {

    UndertowResponseBodyUtils.createNotFoundResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      (Throwable) httpServerExchange.getAttachment(ExceptionHandler.THROWABLE));

  }

  private static void handlePaymentStateException(final HttpServerExchange        httpServerExchange,
                                                  final JsonTransformationPortOut jsonTransformationPortOut,
                                                  final LoggingPortOut            loggingPortOut,
                                                  final Class<?>                  caller) {

    UndertowResponseBodyUtils.createConflictResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      (Throwable) httpServerExchange.getAttachment(ExceptionHandler.THROWABLE));

  }

  private static void handlePaginationInvalidException(final HttpServerExchange        httpServerExchange,
                                                       final JsonTransformationPortOut jsonTransformationPortOut,
                                                       final LoggingPortOut            loggingPortOut,
                                                       final Class<?>                  caller) {

    UndertowResponseBodyUtils.createBadRequestResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      (Throwable) httpServerExchange.getAttachment(ExceptionHandler.THROWABLE));

  }

  private static void handleIllegalArgumentException(final HttpServerExchange        httpServerExchange,
                                                     final JsonTransformationPortOut jsonTransformationPortOut,
                                                     final LoggingPortOut            loggingPortOut,
                                                     final Class<?>                  caller) {

    UndertowResponseBodyUtils.createBadRequestResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      (Throwable) httpServerExchange.getAttachment(ExceptionHandler.THROWABLE));

  }

  private static void handleUnsupportedMediaTypeException(final HttpServerExchange        httpServerExchange,
                                                          final JsonTransformationPortOut jsonTransformationPortOut,
                                                          final LoggingPortOut            loggingPortOut,
                                                          final Class<?>                  caller) {

    UndertowResponseBodyUtils.createUnsupportedMediaTypeResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      (Throwable) httpServerExchange.getAttachment(ExceptionHandler.THROWABLE));

  }

  private static void handlePayloadTooLargeException(final HttpServerExchange        httpServerExchange,
                                                     final JsonTransformationPortOut jsonTransformationPortOut,
                                                     final LoggingPortOut            loggingPortOut,
                                                     final Class<?>                  caller) {

    UndertowResponseBodyUtils.createPayloadTooLargeResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      (Throwable) httpServerExchange.getAttachment(ExceptionHandler.THROWABLE));

  }

  private static void handleGeneralException(final HttpServerExchange        httpServerExchange,
                                             final JsonTransformationPortOut jsonTransformationPortOut,
                                             final LoggingPortOut            loggingPortOut,
                                             final Class<?>                  caller) {

    UndertowResponseBodyUtils.createInternalServerErrorResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      caller,
      (Throwable) httpServerExchange.getAttachment(ExceptionHandler.THROWABLE));

  }

}
