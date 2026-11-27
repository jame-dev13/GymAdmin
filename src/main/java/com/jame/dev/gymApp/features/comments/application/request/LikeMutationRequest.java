package com.jame.dev.gymApp.features.comments.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.features.comments.application.model.LikeMutation;

public record LikeMutationRequest(
   @JsonProperty("mutationType") LikeMutation likeMutation
) {
}
