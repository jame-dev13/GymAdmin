package com.jame.dev.gymApp.features.metrics.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record MonthlyEvolution(
   @JsonProperty("month") String month,
   @JsonProperty("joins") long joins,
   @JsonProperty("downs") long downs
) {
}
