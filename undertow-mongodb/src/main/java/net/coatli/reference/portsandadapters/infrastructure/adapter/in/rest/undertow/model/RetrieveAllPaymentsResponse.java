package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model;

import io.avaje.jsonb.Json;

import java.util.List;

@Json
public record RetrieveAllPaymentsResponse(

  int a0,

  int a1,

  long a2,

  int a3,

  List<RetrieveAllPaymentsItemResponse> a4) {

}
