package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow;

import net.coatli.reference.portsandadapters.application.port.in.UpdatePaymentPortIn;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.UpdatePaymentRequest;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.UpdatePaymentHandlerMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.UndertowQueryParamUtils;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.UndertowRequestBodyUtils;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.UndertowResponseBodyUtils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UpdatePaymentHandler implements HttpHandler {

  private static final String PAYMENT_REFERENCE = "a0";

  private final UpdatePaymentPortIn updatePaymentPortIn;

  private final UpdatePaymentHandlerMapper updatePaymentHandlerMapper;

  private final JsonTransformationPortOut jsonTransformationPortOut;

  private final LoggingPortOut loggingPortOut;

  @Inject
  public UpdatePaymentHandler(final UpdatePaymentPortIn       updatePaymentPortIn,
                              final UpdatePaymentHandlerMapper updatePaymentHandlerMapper,
                              final JsonTransformationPortOut  jsonTransformationPortOut,
                              final LoggingPortOut             loggingPortOut) {

    this.updatePaymentPortIn = updatePaymentPortIn;

    this.updatePaymentHandlerMapper = updatePaymentHandlerMapper;

    this.jsonTransformationPortOut = jsonTransformationPortOut;

    this.loggingPortOut = loggingPortOut;

  }

  @Override
  public void handleRequest(final HttpServerExchange httpServerExchange) throws Exception {

    final var paymentReference = UndertowQueryParamUtils.getQueryParam(httpServerExchange, PAYMENT_REFERENCE);

    final var updatePaymentRequest = UndertowRequestBodyUtils.deserializeBody(
      httpServerExchange,
      jsonTransformationPortOut,
      UpdatePaymentRequest.class);

    final var updatePaymentInput = updatePaymentHandlerMapper.mappingUpdate2UpdatePaymentInput(paymentReference, updatePaymentRequest);

    loggingPortOut.info(
      this.getClass(),
      "[rest.payment.update] request: '{}'",
      jsonTransformationPortOut.toJson(updatePaymentInput));

    final var updatePaymentOutput = updatePaymentPortIn.execute(updatePaymentInput);

    loggingPortOut.info(
      this.getClass(),
      "[rest.payment.update] output: '{}'",
      jsonTransformationPortOut.toJson(updatePaymentOutput));

    UndertowResponseBodyUtils.createOkWithHeaderResponse(
      httpServerExchange,
      loggingPortOut,
      this.getClass(),
      PAYMENT_REFERENCE,
      updatePaymentOutput.paymentReference());

  }

}
