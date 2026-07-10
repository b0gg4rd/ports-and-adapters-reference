package net.coatli.reference.portsandadapters.infrastructure.bootstrap;

import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.config.ExceptionConfig;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.config.RoutesConfig;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.TraceIdHandler;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.UndertowAppUtils;
import net.coatli.reference.portsandadapters.infrastructure.bootstrap.di.DaggerApplicationComponent;
import io.undertow.server.handlers.BlockingHandler;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PaymentBootstrap {

  static void main(final String[] args) throws Exception {

    final var paymentComponent = DaggerApplicationComponent.create();

    UndertowAppUtils.buildAndStart(
      paymentComponent.loggingPortOut(),
      PaymentBootstrap.class,
      new BlockingHandler(
        new TraceIdHandler(
          ExceptionConfig.setup(
            RoutesConfig.routes(paymentComponent),
            paymentComponent.loggingPortOut(),
            PaymentBootstrap.class),
          paymentComponent.loggingPortOut())));

  }

}
