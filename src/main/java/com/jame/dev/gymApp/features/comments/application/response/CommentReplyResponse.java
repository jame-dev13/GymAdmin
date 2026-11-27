package com.jame.dev.gymApp.features.comments.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record CommentReplyResponse(
   @JsonProperty("replyId") String replyId,
   @JsonProperty("replyFrom") String from,
   @JsonProperty("replyText") String replyText,
   @JsonProperty("createdAt") Instant createdAt
) {
}
