package com.jame.dev.gymApp.features.comments.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.features.comments.application.model.Commentable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CommentReplyRequest(
   @JsonProperty("comment")
   @NotBlank
   String comment
) implements Commentable {
}
