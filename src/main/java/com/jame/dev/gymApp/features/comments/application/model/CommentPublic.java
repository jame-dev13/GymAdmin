package com.jame.dev.gymApp.features.comments.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CommentPublic(
   @JsonProperty("from") String from,
   @JsonProperty("comment") String comment,
   @JsonProperty("rate") Double rate,
   @JsonProperty("createdAt") Instant createdAt,
   @JsonProperty("editedAt") Instant editedAt
) {
}
