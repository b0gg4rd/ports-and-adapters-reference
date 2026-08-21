package net.coatli.reference.portsandadapters.infrastructure.adapter.out.logging.slf4j;

import net.coatli.reference.portsandadapters.application.port.out.logging.LoggingPortOut;
import org.slf4j.LoggerFactory;

public class Slf4jLoggingAdapter implements LoggingPortOut {

  @Override
  public void info(final Class<?> caller, final String template, final Object... values) {

    LoggerFactory.getLogger(caller).info(template, values);

  }

  @Override
  public void debug(final Class<?> caller, final String template, final Object... values) {

    LoggerFactory.getLogger(caller).debug(template, values);

  }

  @Override
  public void error(final Class<?> caller, final String message, final Object... values) {

    LoggerFactory.getLogger(caller).error(message, values);

  }

}
