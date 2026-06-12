package net.coatli.reference.portsandadapters.application.port.out.transformation;

public interface JsonTransformationPortOut {

  <T> String toJson(T value);

  <T> T fromJson(String value, Class<T> valueType);

  <T> T fromJson(byte[] value, Class<T> valueType);

}
