package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util;

import io.undertow.server.HttpServerExchange;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UndertowQueryParamUtils {

  public static String getQueryParam(final HttpServerExchange httpServerExchange,
                                     final String             name) {

    final var queryParamDeque = httpServerExchange.getQueryParameters().get(name);

    return
      (null == queryParamDeque || queryParamDeque.isEmpty())
        ? null
        : queryParamDeque.getLast().trim();

  }

}
