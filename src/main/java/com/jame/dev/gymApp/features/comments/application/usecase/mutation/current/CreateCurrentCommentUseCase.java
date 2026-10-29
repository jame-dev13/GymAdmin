package com.jame.dev.gymApp.features.comments.application.usecase.mutation.current;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.request.CommentRequest;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;

public interface CreateCurrentCommentUseCase {
   CommentResponse create(final AuthPrincipal principal, final CommentRequest commentRequest);
}
