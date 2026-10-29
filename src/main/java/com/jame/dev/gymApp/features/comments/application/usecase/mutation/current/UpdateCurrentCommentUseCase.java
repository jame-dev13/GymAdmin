package com.jame.dev.gymApp.features.comments.application.usecase.mutation.current;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.request.CommentRequest;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;

import java.util.UUID;

public interface UpdateCurrentCommentUseCase {
   CommentResponse update(
      final AuthPrincipal principal,
      final UUID commentId,
      final CommentRequest commentRequest
   );
}
