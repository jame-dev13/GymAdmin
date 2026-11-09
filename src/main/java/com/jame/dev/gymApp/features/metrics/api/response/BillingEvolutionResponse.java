package com.jame.dev.gymApp.features.metrics.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.features.metrics.application.model.BillingEvolutionModel;

public record BillingEvolutionResponse(
   @JsonProperty("content") BillingEvolutionModel content
   ) {
}
