package com.jame.dev.gymApp.features.comments.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Objects;

@Builder
public record CommentReply(
   @Id
   @JsonProperty("replyId") String replyId,
   @JsonProperty("replyFrom") String from,
   @JsonProperty("replyText") String replyText,
   @JsonProperty("createdAt") Instant createdAt
) {
   public CommentReply {
      createdAt = Objects.requireNonNullElseGet(createdAt, Instant::now);
   }
}
