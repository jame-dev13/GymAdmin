package com.jame.dev.gymApp.features.current.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CurrentOverviewResponse(
   @JsonProperty("username") String username,
   @JsonProperty("email") String email
) {
}
