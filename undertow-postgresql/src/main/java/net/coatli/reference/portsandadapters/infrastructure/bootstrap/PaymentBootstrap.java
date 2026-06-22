package net.coatli.reference.portsandadapters.infrastructure.bootstrap;

import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.config.ExceptionConfig;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.config.RoutesConfig;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.TraceIdHandler;
import net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util.UndertowAppUtils;
import net.coatli.reference.portsandadapters.infrastructure.bootstrap.di.ApplicationCoreModule;
import net.coatli.reference.portsandadapters.infrastructure.bootstrap.di.InfrastructureAdapterInModule;
import net.coatli.reference.portsandadapters.infrastructure.bootstrap.di.InfrastructureAdapterOutModule;
import io.undertow.server.handlers.BlockingHandler;
import lombok.experimental.UtilityClass;
import org.codejargon.feather.Feather;

@UtilityClass
public class PaymentBootstrap {

  private static final Feather FEATHER = Feather.with(
    new InfrastructureAdapterInModule(),
    new InfrastructureAdapterOutModule(),
    new ApplicationCoreModule());

  static void main(final String[] args) throws Exception {

    final var loggingPortOut = FEATHER.instance(LoggingPortOut.class);

    UndertowAppUtils.buildAndStart(
      loggingPortOut,
      PaymentBootstrap.class,
      new BlockingHandler(
        new TraceIdHandler(
          ExceptionConfig.setup(
            RoutesConfig.routes(FEATHER),
            loggingPortOut,
            PaymentBootstrap.class),
          loggingPortOut,
          PaymentBootstrap.class)));

  }

}
