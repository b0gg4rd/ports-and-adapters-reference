package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util;

import io.undertow.util.HttpString;
import lombok.RequiredArgsConstructor;
import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import org.slf4j.MDC;

import java.util.UUID;

@RequiredArgsConstructor
public class TraceIdHandler implements HttpHandler {

  private static final String X_TRACE_ID = "X-T";

  private static final HttpString X_TRACE_ID_HTTPSTRING = new HttpString(X_TRACE_ID);

  private static final String MDC_KEY = "trace-id";

  private final HttpHandler next;

  private final LoggingPortOut loggingPortOut;

  private final Class<?> caller;

  @Override
  public void handleRequest(final HttpServerExchange httpServerExchange) throws Exception {

    var traceId = UndertowHeaderUtils.getHeader(httpServerExchange, X_TRACE_ID);

    if (null == traceId || traceId.isBlank()) {
      traceId = UUID.randomUUID().toString();
      UndertowHeaderUtils.putRequestHeader(httpServerExchange, X_TRACE_ID_HTTPSTRING, traceId);
    }

    MDC.put(MDC_KEY, traceId);

    loggingPortOut.info(caller, "[rest.trace] '{}' '{}' '{}'",
      httpServerExchange.getRequestMethod(), httpServerExchange.getRequestPath(), traceId);

    httpServerExchange.addExchangeCompleteListener((exchange, nextListener) -> {
      MDC.remove(MDC_KEY);
      nextListener.proceed();
    });

    next.handleRequest(httpServerExchange);

  }

}