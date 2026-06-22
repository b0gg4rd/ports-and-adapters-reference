package net.coatli.reference.portsandadapters.domain.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum PaymentStatus {

  PENDING,

  COMPLETED,

  FAILED;

  private static final Map<String, PaymentStatus> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(Enum::name, Function.identity()));

  public static PaymentStatus findByName(final String name) {
    return BY_NAME.get(name);
  }

}
