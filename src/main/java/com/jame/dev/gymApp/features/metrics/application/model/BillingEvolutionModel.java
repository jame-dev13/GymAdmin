package com.jame.dev.gymApp.features.metrics.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.features.metrics.domain.model.MonthTotal;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record BillingEvolutionModel(
   @JsonProperty("monthlyEvolution") List<MonthTotal> billingEvolutions,
   @JsonProperty("billingInsights") BillingEvoInsights billingEvoInsights
) {
   @Builder
   public record BillingEvoInsights(
      @JsonProperty("totalRevenue") BigDecimal totalRevenue,
      @JsonProperty("bestMonth") String bestMonth,
      @JsonProperty("monthlyAverage") BigDecimal monthlyAverage
   ) {
   }
}
