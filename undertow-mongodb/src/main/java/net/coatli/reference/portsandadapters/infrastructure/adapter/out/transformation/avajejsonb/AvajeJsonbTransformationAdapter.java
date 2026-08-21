package net.coatli.reference.portsandadapters.infrastructure.adapter.out.transformation.avajejsonb;

import io.avaje.json.JsonIoException;
import net.coatli.reference.portsandadapters.application.port.out.transformation.JsonTransformationPortOut;
import io.avaje.jsonb.Jsonb;
import net.coatli.reference.portsandadapters.application.port.out.transformation.exception.JsonTransformationException;

public class AvajeJsonbTransformationAdapter implements JsonTransformationPortOut {

  private final Jsonb jsonb;

  public AvajeJsonbTransformationAdapter() {

    this.jsonb = Jsonb.builder().build();

  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> String toJson(final T value) {

    try {

      return jsonb.type((Class<T>) value.getClass()).toJson(value);

    } catch (final JsonIoException jsonIoException) {

      throw new JsonTransformationException(
        String.format("Error serializing '%s' to JSON", value.getClass().getSimpleName()),
        jsonIoException);

    }

  }

  @Override
  public <T> T fromJson(final String value, final Class<T> valueType) {

    try {

      return jsonb.type(valueType).fromJson(value);

    } catch (final JsonIoException jsonIoException) {

      throw new JsonTransformationException(
        String.format("Error deserializing JSON to '%s'", valueType.getSimpleName()),
        jsonIoException);

    }

  }

  @Override
  public <T> T fromJson(final byte[] value, final Class<T> valueType) {

    try {

      return jsonb.type(valueType).fromJson(value);

    } catch (final JsonIoException jsonIoException) {

      throw new JsonTransformationException(
        String.format("Error deserializing JSON to '%s'", valueType.getSimpleName()),
        jsonIoException);

    }

  }

}
