package com.jame.dev.gymApp.features.auth.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.features.auth.application.model.LoginUserType;
import lombok.Builder;

@Builder
public record SignInResponse(
        @JsonProperty("userType") LoginUserType type,
        @JsonIgnore String email,
        @JsonIgnore String access,
        @JsonIgnore String refresh
) {
}
