package com.jame.dev.gymApp.features.comments.domain.repository;

import java.util.UUID;

public interface CommentValidationRepository {
   boolean existsById(UUID commentId);

   boolean existsByIdAndAuthorId(UUID commentId, Long authorId);
}
