package com.jame.dev.gymApp.features.comments.domain.repository;

import com.jame.dev.gymApp.features.comments.domain.model.CommentDocument;

import java.util.UUID;

public interface CommentMutationRepository {
   CommentDocument save(final CommentDocument commentDocument);

   void deleteById(final UUID commentId);
}
