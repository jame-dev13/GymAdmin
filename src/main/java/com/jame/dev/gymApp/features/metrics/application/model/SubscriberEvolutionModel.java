package com.jame.dev.gymApp.features.metrics.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record SubscriberEvolutionModel(
   @JsonProperty("monthlyEvolution") List<MonthlyEvolution> monthlyEvolution,
   @JsonProperty("evoInsights") EvoInsights evoInsights
) {
}
