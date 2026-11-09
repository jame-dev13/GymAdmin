package com.jame.dev.gymApp.features.metrics.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.features.metrics.application.model.SubscriptionAnnualResumeModel;

public record SubscriptionAnnualResumeResponseV2(
   @JsonProperty("content") SubscriptionAnnualResumeModel subscriptionAnnualResumeModel
   ) {
}
