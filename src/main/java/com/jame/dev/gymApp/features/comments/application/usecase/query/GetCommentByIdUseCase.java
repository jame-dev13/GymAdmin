package com.jame.dev.gymApp.features.comments.application.usecase.query;

import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;

import java.util.UUID;

public interface GetCommentByIdUseCase {
   CommentResponse getById(final UUID commentId);
}
