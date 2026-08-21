package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow;

import net.coatli.reference.portsandadapters.application.port.in.DeletePaymentPortIn;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.DeletePaymentHandlerMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.UndertowQueryParamUtils;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.UndertowResponseBodyUtils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DeletePaymentHandler implements HttpHandler {

  private static final String PAYMENT_REFERENCE = "a0";

  private final DeletePaymentPortIn deletePaymentPortIn;

  private final DeletePaymentHandlerMapper deletePaymentHandlerMapper;

  private final JsonTransformationPortOut jsonTransformationPortOut;

  private final LoggingPortOut loggingPortOut;

  @Inject
  public DeletePaymentHandler(final DeletePaymentPortIn       deletePaymentPortIn,
                              final DeletePaymentHandlerMapper deletePaymentHandlerMapper,
                              final JsonTransformationPortOut  jsonTransformationPortOut,
                              final LoggingPortOut             loggingPortOut) {

    this.deletePaymentPortIn = deletePaymentPortIn;

    this.deletePaymentHandlerMapper = deletePaymentHandlerMapper;

    this.jsonTransformationPortOut = jsonTransformationPortOut;

    this.loggingPortOut = loggingPortOut;

  }

  @Override
  public void handleRequest(final HttpServerExchange httpServerExchange) throws Exception {

    final var paymentReference = UndertowQueryParamUtils.getQueryParam(httpServerExchange, PAYMENT_REFERENCE);

    final var deletePaymentInput = deletePaymentHandlerMapper.mappingPaymentReference2DeletePaymentInput(paymentReference);

    loggingPortOut.info(
      this.getClass(),
      "[rest.payment.delete] request: '{}'",
      jsonTransformationPortOut.toJson(deletePaymentInput));

    final var deletePaymentOutput = deletePaymentPortIn.execute(deletePaymentInput);

    loggingPortOut.info(
      this.getClass(),
      "[rest.payment.delete] output: '{}'",
      jsonTransformationPortOut.toJson(deletePaymentOutput));

    UndertowResponseBodyUtils.createNoContentResponse(
      httpServerExchange,
      loggingPortOut,
      this.getClass());

  }

}
