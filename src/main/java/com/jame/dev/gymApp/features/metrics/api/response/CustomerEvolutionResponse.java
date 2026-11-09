package com.jame.dev.gymApp.features.metrics.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.features.metrics.application.model.CustomerEvolutionModel;

public record CustomerEvolutionResponse(
   @JsonProperty("content") CustomerEvolutionModel content
   ) {
}
