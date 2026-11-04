package com.jame.dev.gymApp.features.metrics.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record OverviewResponse(
   @JsonProperty("activeMembers") long activeMembers,
   @JsonProperty("monthlyRevenue") BigDecimal monthlyRevenue,
   @JsonProperty("expiringSubscriptions") long expiringSubscriptions,
   @JsonProperty("newSignups") long newSignups
) {
}
