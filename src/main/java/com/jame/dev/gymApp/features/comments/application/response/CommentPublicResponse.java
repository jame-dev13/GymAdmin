package com.jame.dev.gymApp.features.comments.application.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.features.comments.application.model.CommentPublic;
import lombok.Builder;

import java.util.Set;

@Builder
public record CommentPublicResponse(
   @JsonProperty("content") Set<CommentPublic> content
   ) {
}

