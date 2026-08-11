package net.coatli.reference.portsandadapters.infrastructure.bootstrap;

import net.coatli.reference.portsandadapters.infrastructure.bootstrap.util.ResourcePropertiesLoader;
import lombok.Getter;

import java.util.Properties;

public enum ApplicationProperties {

  APPLICATION_PROPERTIES;

  private static final String DEFAULT_PATH = "/conf/application.properties";

  public String get(final String key) {

    return instance.getProperty(key);

  }

  @Getter
  private final Properties instance = ResourcePropertiesLoader.load(ApplicationProperties.class, DEFAULT_PATH);

}
