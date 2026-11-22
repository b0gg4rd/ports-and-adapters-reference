package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.config;

import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.AllowedMethods;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.MethodNotAllowedHandler;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.OpenApiHandlers;
import net.coatli.reference.portsandadapters.infrastructure.bootstrap.di.ApplicationComponent;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoutesConfig {

  public static HttpHandler routes(final ApplicationComponent paymentComponent) {

    final var allowedMethods = new AllowedMethods();

    final var routing =
      Handlers
        .routing()
        .setInvalidMethodHandler(
          new MethodNotAllowedHandler(
            allowedMethods,
            paymentComponent.jsonTransformationPortOut(),
            paymentComponent.loggingPortOut()));

    add(routing, allowedMethods, Methods.GET,    "/api/v1/ping",                        ResponseCodeHandler.HANDLE_200);
    add(routing, allowedMethods, Methods.POST,   "/api/v1/payments",                    paymentComponent.createPaymentHandler());
    add(routing, allowedMethods, Methods.GET,    "/api/v1/payments",                    paymentComponent.retrieveAllPaymentsHandler());
    add(routing, allowedMethods, Methods.PATCH,  "/api/v1/payments/{paymentReference}", paymentComponent.updatePaymentHandler());
    add(routing, allowedMethods, Methods.DELETE, "/api/v1/payments/{paymentReference}", paymentComponent.deletePaymentHandler());
    add(routing, allowedMethods, Methods.GET,    "/openapi",                            OpenApiHandlers.SWAGGER_UI);
    add(routing, allowedMethods, Methods.GET,    "/openapi/*",                          OpenApiHandlers.OPENAPI);

    return routing;

  }

  private static void add(final RoutingHandler routing,
                          final AllowedMethods allowedMethods,
                          final HttpString     method,
                          final String         template,
                          final HttpHandler    handler) {

    routing.add(method, template, handler);

    allowedMethods.register(method, template);

  }

}
