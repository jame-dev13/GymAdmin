package com.jame.dev.gymApp.features.comments.application.usecase.mutation;

import com.jame.dev.gymApp.features.comments.application.request.CommentRequest;

import java.util.UUID;

public interface UpdateCommentUseCase {
   void update(final UUID commentId, final CommentRequest commentRequest);
}
