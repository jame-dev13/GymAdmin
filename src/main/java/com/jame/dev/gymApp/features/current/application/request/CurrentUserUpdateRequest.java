package com.jame.dev.gymApp.features.current.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.infrastructure.annotation.EmailValid;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

public record CurrentUserUpdateRequest(
   @JsonProperty("name")
   @NotBlank(message = "name is required.")
   String name,
   @JsonProperty("email")
   @EmailValid(message = "Invalid email format.") String email,
   @JsonProperty("password")
   @Nullable
   String password
) {
}
