package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow;

import net.coatli.reference.portsandadapters.application.port.in.RetrieveAllPaymentsPortIn;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model.mapper.RetrieveAllPaymentsHandlerMapper;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.UndertowQueryParamUtils;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.UndertowResponseBodyUtils;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RetrieveAllPaymentsHandler implements HttpHandler {

  private static final String PAGE_PARAM = "page";

  private static final String SIZE_PARAM = "size";

  private final RetrieveAllPaymentsPortIn retrieveAllPaymentsPortIn;

  private final RetrieveAllPaymentsHandlerMapper retrieveAllPaymentsHandlerMapper;

  private final JsonTransformationPortOut jsonTransformationPortOut;

  private final LoggingPortOut loggingPortOut;

  @Inject
  public RetrieveAllPaymentsHandler(final RetrieveAllPaymentsPortIn        retrieveAllPaymentsPortIn,
                                    final RetrieveAllPaymentsHandlerMapper  retrieveAllPaymentsHandlerMapper,
                                    final JsonTransformationPortOut         jsonTransformationPortOut,
                                    final LoggingPortOut                    loggingPortOut) {

    this.retrieveAllPaymentsPortIn = retrieveAllPaymentsPortIn;

    this.retrieveAllPaymentsHandlerMapper = retrieveAllPaymentsHandlerMapper;

    this.jsonTransformationPortOut = jsonTransformationPortOut;

    this.loggingPortOut = loggingPortOut;

  }

  @Override
  public void handleRequest(final HttpServerExchange httpServerExchange) throws Exception {

    final var retrieveAllPaymentsInput = retrieveAllPaymentsHandlerMapper.mappingQueryParams2RetrieveAllPaymentsInput(
      UndertowQueryParamUtils.getQueryParam(httpServerExchange, PAGE_PARAM),
      UndertowQueryParamUtils.getQueryParam(httpServerExchange, SIZE_PARAM));

    loggingPortOut.info(
      this.getClass(),
      "[rest.payment.retrieve] request: '{}'",
      jsonTransformationPortOut.toJson(retrieveAllPaymentsInput));

    final var retrieveAllPaymentsOutput = retrieveAllPaymentsPortIn.execute(retrieveAllPaymentsInput);

    loggingPortOut.info(
      this.getClass(),
      "[rest.payment.retrieve] output: '{}'",
      jsonTransformationPortOut.toJson(retrieveAllPaymentsOutput));

    UndertowResponseBodyUtils.createOkResponse(
      httpServerExchange,
      loggingPortOut,
      this.getClass(),
      jsonTransformationPortOut.toJson(
        retrieveAllPaymentsHandlerMapper.mappingRetrieveAllPaymentsOutput2RetrieveAllPaymentsResponse(retrieveAllPaymentsOutput)));

  }

}
