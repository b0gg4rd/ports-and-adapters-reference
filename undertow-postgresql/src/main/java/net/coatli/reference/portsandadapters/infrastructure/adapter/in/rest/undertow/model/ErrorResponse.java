package net.coatli.reference.portsandadapters.infrastructure.adapter.in.rest.undertow.model;

import io.avaje.jsonb.Json;

@Json
public record ErrorResponse(

  String detail) {

}
