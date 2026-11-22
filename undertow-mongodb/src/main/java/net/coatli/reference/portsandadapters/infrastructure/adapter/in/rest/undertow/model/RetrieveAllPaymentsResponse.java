package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model;

import io.avaje.jsonb.Json;

import java.util.List;

@Json
public record RetrieveAllPaymentsResponse(

  int page,

  int size,

  long totalElements,

  int totalPages,

  List<RetrieveAllPaymentsItemResponse> content) {

}
