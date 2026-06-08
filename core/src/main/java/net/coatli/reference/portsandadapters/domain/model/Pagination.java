package net.coatli.reference.portsandadapters.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(
  chain = true,
  fluent = false)
@NoArgsConstructor
public class Pagination {

  private int page;

  private int size;

}
