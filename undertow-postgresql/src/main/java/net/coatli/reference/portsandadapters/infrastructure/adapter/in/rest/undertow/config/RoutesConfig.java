package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.config;

import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.OpenApiHandlers;
import net.coatli.reference.portsandadapters.infrastructure.bootstrap.di.ApplicationComponent;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.util.Methods;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RoutesConfig {

  public static HttpHandler routes(final ApplicationComponent paymentComponent) {

    return
      Handlers
        .routing()
        .add(
          Methods.GET,
          "/api/v1/ping",
          ResponseCodeHandler.HANDLE_200)
        .add(
          Methods.POST,
          "/api/v1/payments",
          paymentComponent.createPaymentHandler())
        .add(
          Methods.GET,
          "/api/v1/payments",
          paymentComponent.retrieveAllPaymentsHandler())
        .add(
          Methods.PATCH,
          "/api/v1/payments/{a0}",
          paymentComponent.updatePaymentHandler())
        .add(
          Methods.DELETE,
          "/api/v1/payments/{a0}",
          paymentComponent.deletePaymentHandler())
        .add(
          Methods.GET,
          "/openapi/*",
          OpenApiHandlers.OPENAPI)
        .add(
          Methods.GET,
          "/swagger-ui",
          OpenApiHandlers.SWAGGER_UI_REDIRECT)
        .add(
          Methods.GET,
          "/swagger-ui/*",
          OpenApiHandlers.SWAGGER_UI);

  }

}
