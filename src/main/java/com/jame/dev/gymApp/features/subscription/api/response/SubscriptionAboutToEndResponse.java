package com.jame.dev.gymApp.features.subscription.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.features.subscription.domain.model.Membership;
import com.jame.dev.gymApp.features.subscription.domain.model.SubscriptionStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SubscriptionAboutToEndResponse(
   @JsonProperty("member") String member,
   @JsonProperty("membership") Membership membership,
   @JsonProperty("price") BigDecimal price,
   @JsonProperty("endsOn") LocalDate endsOn,
   @JsonProperty("status") SubscriptionStatus status
) {
}
