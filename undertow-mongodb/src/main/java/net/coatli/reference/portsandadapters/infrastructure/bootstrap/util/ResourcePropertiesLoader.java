package net.coatli.reference.portsandadapters.infrastructure.bootstrap.util;

import lombok.experimental.UtilityClass;

import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.regex.Pattern;

@UtilityClass
public class ResourcePropertiesLoader {

  private static final String ENV_VAR_REGEX = "\\$\\{([^}:]+)(?::([^}]*))?\\}";

  private static final int ENV_VAR_NAME_GROUP = 1;

  private static final int DEFAULT_VALUE_GROUP = 2;

  public static Properties load(final Class<?> anchor, final String path) {

    return
      Optional
        .ofNullable(anchor.getResourceAsStream(path))
        .map(readProperties(new Properties()))
        .map(resolveEnvVarsValues())
        .orElseThrow(() -> new IllegalStateException("Could not find properties file at " + path));

  }

  private static Function<InputStream, Properties> readProperties(final Properties properties) {

    return (inputStream) -> {

      try (inputStream) {

        properties.load(inputStream);

        return properties;

      } catch (final Exception exception) {

        throw new IllegalStateException("Failed to load properties", exception);

      }

    };

  }

  private static Function<Properties, Properties> resolveEnvVarsValues() {

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

  private static Function<Object, String> resolveEnvVarValue() {

    return value -> Pattern
      .compile(ENV_VAR_REGEX)
      .matcher(value.toString())
      .replaceAll(match -> Optional
        .ofNullable(System.getenv(match.group(ENV_VAR_NAME_GROUP)))
        .orElse(match.group(DEFAULT_VALUE_GROUP)));

  }

}
