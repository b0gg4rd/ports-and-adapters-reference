package net.coatli.reference.portsandadapters.infrastructure.bootstrap;

import lombok.Getter;

import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.regex.Pattern;

public enum ApplicationProperties {

  APPLICATION_PROPERTIES;

  private static final String DEFAULT_PATH = "/conf/application.properties";

  private static final String ENV_VAR_REGEX = "\\$\\{([^}:]+)(?::([^}]*))?\\}";

  private static final int ENV_VAR_NAME_GROUP = 1;

  private static final int DEFAULT_VALUE_GROUP = 2;

  public String get(final String key) {

    return instance.getProperty(key);

  }

  @Getter
  private final Properties instance = initialize();

  private Properties initialize() {

    return
      Optional
        .ofNullable(ApplicationProperties.class.getResourceAsStream(DEFAULT_PATH))
        .map(load(new Properties()))
        .map(resolveEnvVarsValues())
        .orElseThrow(() -> new IllegalStateException("Could not find application properties file at " + DEFAULT_PATH));

  }

  private Function<InputStream, Properties> load(final Properties properties) {

    return (inputStream) -> {

      try (inputStream) {

        properties.load(inputStream);

        return properties;

      } catch (final Exception exception) {

        throw new IllegalStateException("Failed to load application properties", exception);

      }

    };

  }

  private Function<Properties, Properties> resolveEnvVarsValues() {

    return (properties) -> {

      properties.forEach(
        (key, value) -> properties.setProperty(
          key.toString(),
          Optional
            .ofNullable(value)
            .map(resolveEnvVarValue())
            .orElse(null)));

      return properties;

    };

  }

  private Function<Object, String> resolveEnvVarValue() {

    return value -> Pattern
      .compile(ENV_VAR_REGEX)
      .matcher(value.toString())
      .replaceAll(match -> Optional
        .ofNullable(System.getenv(match.group(ENV_VAR_NAME_GROUP)))
        .orElse(match.group(DEFAULT_VALUE_GROUP)));

  }

}
