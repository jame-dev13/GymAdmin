package com.jame.dev.gymApp.features.comments.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.features.comments.domain.model.CommentType;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record CommentResponse(
   @JsonProperty("commentId") UUID commentId,
   @JsonProperty("from") String from,
   @JsonProperty("comment") String comment,
   @JsonProperty("commentType") CommentType commentType,
   @JsonProperty("rate") Double rate,
   @JsonProperty("createdAt") Instant createdAt,
   @JsonProperty("editedAt") Instant editedAt
) {
}
