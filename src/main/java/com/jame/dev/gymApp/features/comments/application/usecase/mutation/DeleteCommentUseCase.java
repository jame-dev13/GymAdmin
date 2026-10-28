package com.jame.dev.gymApp.features.comments.application.usecase.mutation;

import java.util.UUID;

public interface DeleteCommentUseCase {
   void delete(final UUID commentId);
}
