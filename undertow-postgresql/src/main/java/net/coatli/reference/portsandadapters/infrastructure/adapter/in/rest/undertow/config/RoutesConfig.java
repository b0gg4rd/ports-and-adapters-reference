package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.config;

import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.CreatePaymentHandler;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.DeletePaymentHandler;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.RetrieveAllPaymentsHandler;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.UpdatePaymentHandler;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.ResponseCodeHandler;
import io.undertow.util.Methods;
import lombok.experimental.UtilityClass;
import org.codejargon.feather.Feather;

@UtilityClass
public class RoutesConfig {

  public static HttpHandler routes(final Feather feather) {

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
          feather.instance(CreatePaymentHandler.class))
        .add(
          Methods.GET,
          "/api/v1/payments",
          feather.instance(RetrieveAllPaymentsHandler.class))
        .add(
          Methods.PUT,
          "/api/v1/payments/{a0}",
          feather.instance(UpdatePaymentHandler.class))
        .add(
          Methods.DELETE,
          "/api/v1/payments/{a0}",
          feather.instance(DeletePaymentHandler.class));

  }

}
