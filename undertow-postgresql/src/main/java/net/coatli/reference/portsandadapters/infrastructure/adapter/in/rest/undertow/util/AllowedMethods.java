package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util;

import io.undertow.util.HttpString;
import io.undertow.util.PathTemplateMatcher;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AllowedMethods {

  private static final String LIST_SEPARATOR = ", ";

  private final PathTemplateMatcher<Set<HttpString>> matcher = new PathTemplateMatcher<>();

  public AllowedMethods register(final HttpString method,
                                 final String     template) {

    var methods = matcher.get(template);

    if (null == methods) {

      methods = new LinkedHashSet<>();

      matcher.add(template, methods);

    }

    methods.add(method);

    return this;

  }

  public String allowHeaderFor(final String path) {

    final var match = matcher.match(path);

    if (null == match) {

      return null;

    }

    return
      match
        .getValue()
        .stream()
        .map(HttpString::toString)
        .collect(Collectors.joining(LIST_SEPARATOR));

  }

}
