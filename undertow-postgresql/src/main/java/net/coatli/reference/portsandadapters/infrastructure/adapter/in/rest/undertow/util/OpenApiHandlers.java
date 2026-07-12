package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util;

import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.RedirectHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.util.MimeMappings;
import lombok.experimental.UtilityClass;

@UtilityClass
public class OpenApiHandlers {

  public static final HttpHandler OPENAPI =
    Handlers
      .resource(new ClassPathResourceManager(OpenApiHandlers.class.getClassLoader(), ""))
      .setMimeMappings(MimeMappings.builder().addMapping("yml", "application/yaml").build());

  public static final HttpHandler SWAGGER_UI =
    Handlers
      .resource(new ClassPathResourceManager(OpenApiHandlers.class.getClassLoader(), "openapi"))
      .addWelcomeFiles("index.html");

  public static final HttpHandler SWAGGER_UI_REDIRECT = new RedirectHandler("/swagger-ui/");

}
