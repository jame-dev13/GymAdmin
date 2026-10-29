package com.jame.dev.gymApp.features.comments.application.usecase.mutation.current;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;

import java.util.UUID;

public interface DeleteCurrentCommentUseCase {
   void delete(final AuthPrincipal principal, final UUID commentId);
}
