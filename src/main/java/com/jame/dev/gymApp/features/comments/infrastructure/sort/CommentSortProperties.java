package com.jame.dev.gymApp.features.comments.infrastructure.sort;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CommentSortProperties {
   ID("id", "id"),
   FROM("from", "from"),
   TYPE("type", "commentType"),
   CREATED_AT("creation", "createdAt");

   private final String exposedProperty;
   private final String appProperty;
}
