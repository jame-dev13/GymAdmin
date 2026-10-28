package com.jame.dev.gymApp.features.comments.application.usecase.mutation;

import com.jame.dev.gymApp.features.comments.application.request.CommentRequest;

public interface CreateCommentUseCase {
   void create(CommentRequest commentRequest);
}
