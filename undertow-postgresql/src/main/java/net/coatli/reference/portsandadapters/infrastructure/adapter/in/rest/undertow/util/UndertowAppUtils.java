package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.util;

import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import net.coatli.reference.portsandadapters.infrastructure.bootstrap.ApplicationProperties;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.HttpHandler;
import lombok.experimental.UtilityClass;
import org.xnio.Options;

@UtilityClass
public class UndertowAppUtils {

  private static final String HOST = "host";

  private static final String PORT = "port";

  private static final String NAME = "name";

  private static final String VERSION = "version";

  private static final String COMMIT = "commit";

  private static final int BUFFER_SIZE = 1024 * 64;

  private static final int IO_THREADS = Runtime.getRuntime().availableProcessors() * 4;

  private static final int WORKER_THREADS = IO_THREADS * 2;

  private static final int BACKLOG_SIZE = 10000;

  public static void buildAndStart(final LoggingPortOut loggingPortOut,
                                   final Class          clazz,
                                   final HttpHandler    routes) {

    loggingPortOut.info(
      clazz,
      "[rest.app.bootstrap] event=app_starting app={} commit={} version={} port={}",
      ApplicationProperties.APPLICATION_PROPERTIES.get(NAME),
      ApplicationProperties.APPLICATION_PROPERTIES.get(COMMIT),
      ApplicationProperties.APPLICATION_PROPERTIES.get(VERSION),
      ApplicationProperties.APPLICATION_PROPERTIES.get(PORT));

    UndertowAppUtils
      .buildUndertow(routes)
      .start();

    loggingPortOut.info(
      clazz,
      "[rest.app.bootstrap] event=app_started app={} commit={} version={} port={}",
      ApplicationProperties.APPLICATION_PROPERTIES.get(NAME),
      ApplicationProperties.APPLICATION_PROPERTIES.get(COMMIT),
      ApplicationProperties.APPLICATION_PROPERTIES.get(VERSION),
      ApplicationProperties.APPLICATION_PROPERTIES.get(PORT));

  }

  private static Undertow buildUndertow(final HttpHandler routes) {

    return
      Undertow
        .builder()
        .addHttpListener(
          Integer.parseInt(ApplicationProperties.APPLICATION_PROPERTIES.get(PORT)),
          ApplicationProperties.APPLICATION_PROPERTIES.get(HOST))
        .setBufferSize(BUFFER_SIZE)
        .setIoThreads(IO_THREADS)
        .setWorkerThreads(WORKER_THREADS)
        .setSocketOption(Options.BACKLOG, BACKLOG_SIZE)
        .setServerOption(UndertowOptions.ALWAYS_SET_KEEP_ALIVE, false)
        .setServerOption(UndertowOptions.ALWAYS_SET_DATE, true)
        .setServerOption(UndertowOptions.RECORD_REQUEST_START_TIME, false)
        .setHandler(routes)
        .build();

  }

}
