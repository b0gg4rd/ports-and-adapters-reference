package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.PathTemplateMatch;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UndertowPathParamUtils {

  public static String getPathParam(final HttpServerExchange httpServerExchange,
                                    final String             name) {

    final var pathTemplateMatch = httpServerExchange.getAttachment(PathTemplateMatch.ATTACHMENT_KEY);

    if (null == pathTemplateMatch) {

      return null;

    }

    final var value = pathTemplateMatch.getParameters().get(name);

    return
      null == value
        ? null
        : value.trim();

  }

}
