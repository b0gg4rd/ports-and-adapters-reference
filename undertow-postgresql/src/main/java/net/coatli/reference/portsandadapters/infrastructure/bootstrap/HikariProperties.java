package net.coatli.reference.portsandadapters.infrastructure.bootstrap;

import net.coatli.reference.portsandadapters.infrastructure.bootstrap.util.ResourcePropertiesLoader;
import lombok.Getter;

import java.util.Properties;

public enum HikariProperties {

  HIKARI_PROPERTIES;

  private static final String DEFAULT_PATH = "/conf/hikari.properties";

  @Getter
  private final Properties instance = ResourcePropertiesLoader.load(HikariProperties.class, DEFAULT_PATH);

}
