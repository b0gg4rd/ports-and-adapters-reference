package net.coatli.reference.portsandadapters.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(
  chain = true,
  fluent = false)
@NoArgsConstructor
public class Page<T> {

  private Pagination pagination;

  private List<T> content;

  private long totalElements;

  private int totalPages;

}
