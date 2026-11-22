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

  private static final String TRACE_ID_HEADER = "Trace-Id";

  private static final HttpString TRACE_ID_HEADER_HTTPSTRING = new HttpString(TRACE_ID_HEADER);

  private static final String MDC_KEY = "trace-id";

  private final HttpHandler next;

  private final LoggingPortOut loggingPortOut;

  @Override
  public void handleRequest(final HttpServerExchange httpServerExchange) throws Exception {

    var traceId = UndertowHeaderUtils.getLastHeaderValue(httpServerExchange, TRACE_ID_HEADER);

    if (null == traceId || traceId.isBlank()) {
      traceId = UUID.randomUUID().toString();
      UndertowHeaderUtils.putRequestHeader(httpServerExchange, TRACE_ID_HEADER_HTTPSTRING, traceId);
    }

    UndertowHeaderUtils.putResponseHeader(httpServerExchange, TRACE_ID_HEADER_HTTPSTRING, traceId);

    MDC.put(MDC_KEY, traceId);

    final var startTime = System.nanoTime();

    loggingPortOut.info(
      this.getClass(),
      "[rest.trace.start] '{}' '{}' traceId '{}'",
      httpServerExchange.getRequestMethod(), httpServerExchange.getRequestPath(), traceId);

    httpServerExchange.addExchangeCompleteListener((exchange, nextListener) -> {

      loggingPortOut.info(
        this.getClass(),
        "[rest.trace.end] '{}' '{}' '{}' duration '{}'",
        exchange.getRequestMethod(), exchange.getRequestPath(),
        exchange.getStatusCode(), (System.nanoTime() - startTime) / 1_000_000);

      MDC.remove(MDC_KEY);
      nextListener.proceed();

    });

    next.handleRequest(httpServerExchange);

  }

}
