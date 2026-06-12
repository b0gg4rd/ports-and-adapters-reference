package net.coatli.reference.portsandadapters.application.port.out.logging;

public interface LoggingPortOut {

  void info(Class<?> caller, String message, Object... values);

  void debug(Class<?> caller, String message, Object... values);

  void error(Class<?> caller, String message, Object... values);

}
