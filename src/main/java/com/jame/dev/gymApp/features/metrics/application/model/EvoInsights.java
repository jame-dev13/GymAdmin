package com.jame.dev.gymApp.features.metrics.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record EvoInsights(
   @JsonProperty("joiningTotal") long joiningTotal,
   @JsonProperty("downingTotal") long downingTotal,
   @JsonProperty("netMovement") long netMovement
) {
}
