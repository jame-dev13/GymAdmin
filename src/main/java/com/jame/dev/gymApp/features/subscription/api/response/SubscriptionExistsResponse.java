package com.jame.dev.gymApp.features.subscription.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SubscriptionExistsResponse(
   @JsonProperty("exists") boolean exists
) {
}
