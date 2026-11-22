package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util;

import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MethodNotAllowedHandler implements HttpHandler {

  private final AllowedMethods allowedMethods;

  private final JsonTransformationPortOut jsonTransformationPortOut;

  private final LoggingPortOut loggingPortOut;

  @Override
  public void handleRequest(final HttpServerExchange httpServerExchange) throws Exception {

    UndertowResponseBodyUtils.createMethodNotAllowedResponse(
      httpServerExchange,
      jsonTransformationPortOut,
      loggingPortOut,
      this.getClass(),
      allowedMethods.allowHeaderFor(httpServerExchange.getRelativePath()));

  }

}
