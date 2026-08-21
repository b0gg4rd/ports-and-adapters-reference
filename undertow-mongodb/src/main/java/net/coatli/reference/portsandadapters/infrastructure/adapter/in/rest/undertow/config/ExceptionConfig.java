package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.config;

import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentInputException;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentNotFoundException;
import net.coatli.reference.portsandadapters.application.port.in.exception.PaymentStateException;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.UndertowResponseBodyUtils;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionConfig {

  public static HttpHandler setup(final HttpHandler    routes,
                                  final LoggingPortOut loggingPortOut,
                                  final Class<?>       caller) {

    return Handlers
      .exceptionHandler(routes)
      .addExceptionHandler(
        PaymentInputException.class,
        httpServerExchange -> handlePaymentInputException(httpServerExchange, loggingPortOut, caller))
      .addExceptionHandler(
        PaymentNotFoundException.class,
        httpServerExchange -> handlePaymentNotFoundException(httpServerExchange, loggingPortOut, caller))
      .addExceptionHandler(
        PaymentStateException.class,
        httpServerExchange -> handlePaymentStateException(httpServerExchange, loggingPortOut, caller))
      .addExceptionHandler(
        IllegalArgumentException.class,
        httpServerExchange -> handleIllegalArgumentException(httpServerExchange, loggingPortOut, caller))
      .addExceptionHandler(
        Exception.class,
        httpServerExchange -> handleGeneralException(httpServerExchange, loggingPortOut, caller));

  }

  private static void handlePaymentInputException(final HttpServerExchange httpServerExchange,
                                                  final LoggingPortOut     loggingPortOut,
                                                  final Class<?>           caller) {

    UndertowResponseBodyUtils.createBadRequestResponse(
      httpServerExchange,
      loggingPortOut,
      caller,
      (Throwable) httpServerExchange.getAttachment(ExceptionHandler.THROWABLE));

  }

  private static void handlePaymentNotFoundException(final HttpServerExchange httpServerExchange,
                                                     final LoggingPortOut     loggingPortOut,
                                                     final Class<?>           caller) {

    UndertowResponseBodyUtils.createNotFoundResponse(
      httpServerExchange,
      loggingPortOut,
      caller,
      (Throwable) httpServerExchange.getAttachment(ExceptionHandler.THROWABLE));

  }

  private static void handlePaymentStateException(final HttpServerExchange httpServerExchange,
                                                   final LoggingPortOut     loggingPortOut,
                                                   final Class<?>           caller) {

    UndertowResponseBodyUtils.createConflictResponse(
      httpServerExchange,
      loggingPortOut,
      caller,
      (Throwable) httpServerExchange.getAttachment(ExceptionHandler.THROWABLE));

  }

  private static void handleIllegalArgumentException(final HttpServerExchange httpServerExchange,
                                                     final LoggingPortOut     loggingPortOut,
                                                     final Class<?>           caller) {

    UndertowResponseBodyUtils.createBadRequestResponse(
      httpServerExchange,
      loggingPortOut,
      caller,
      (Throwable) httpServerExchange.getAttachment(ExceptionHandler.THROWABLE));

  }

  private static void handleGeneralException(final HttpServerExchange httpServerExchange,
                                             final LoggingPortOut     loggingPortOut,
                                             final Class<?>           caller) {

    UndertowResponseBodyUtils.createInternalServerErrorResponse(
      httpServerExchange,
      loggingPortOut,
      caller,
      (Throwable) httpServerExchange.getAttachment(ExceptionHandler.THROWABLE));

  }

}
