package com.jame.dev.gymApp.features.metrics.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.features.metrics.application.model.SubscriberEvolutionModel;

public record SubscriberEvolutionResponse(
   @JsonProperty("content") SubscriberEvolutionModel content
) {
}
