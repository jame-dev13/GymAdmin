package com.jame.dev.gymApp.features.current.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.features.user.domain.model.Role;
import lombok.Builder;

import java.util.Set;

@Builder
public record CurrentUserResponse(
   @JsonProperty("name") String name,
   @JsonProperty("email") String email,
   @JsonProperty("roles") Set<Role> roles
   ) {
}
