package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow;

import net.coatli.reference.portsandadapters.application.port.in.CreatePaymentPortIn;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.CreatePaymentRequest;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.CreatePaymentHandlerMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.UndertowRequestBodyUtils;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.UndertowResponseBodyUtils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CreatePaymentHandler implements HttpHandler {

  private static final String PAYMENT_REFERENCE_HEADER = "a0";

  private final CreatePaymentPortIn createPaymentPortIn;

  private final CreatePaymentHandlerMapper createPaymentHandlerMapper;

  private final JsonTransformationPortOut jsonTransformationPortOut;

  private final LoggingPortOut loggingPortOut;

  @Inject
  public CreatePaymentHandler(final CreatePaymentPortIn       createPaymentPortIn,
                              final CreatePaymentHandlerMapper createPaymentHandlerMapper,
                              final JsonTransformationPortOut  jsonTransformationPortOut,
                              final LoggingPortOut             loggingPortOut) {

    this.createPaymentPortIn = createPaymentPortIn;

    this.createPaymentHandlerMapper = createPaymentHandlerMapper;

    this.jsonTransformationPortOut = jsonTransformationPortOut;

    this.loggingPortOut = loggingPortOut;

  }

  @Override
  public void handleRequest(final HttpServerExchange httpServerExchange) throws Exception {

    final var createPaymentRequest = UndertowRequestBodyUtils.deserializeBody(
      httpServerExchange,
      jsonTransformationPortOut,
      CreatePaymentRequest.class);

    loggingPortOut.info(
      this.getClass(),
      "[rest.payment.create] request: '{}'",
      jsonTransformationPortOut.toJson(createPaymentRequest));

    final var createPaymentOutput = createPaymentPortIn.execute(
      createPaymentHandlerMapper.mappingCreatePaymentRequest2CreatePaymentInput(createPaymentRequest));

    loggingPortOut.info(
      this.getClass(),
      "[rest.payment.create] output: '{}'",
      jsonTransformationPortOut.toJson(createPaymentOutput));

    UndertowResponseBodyUtils.createCreatedResponse(
      httpServerExchange,
      loggingPortOut,
      this.getClass(),
      PAYMENT_REFERENCE_HEADER,
      createPaymentOutput.paymentReference());

  }

}
