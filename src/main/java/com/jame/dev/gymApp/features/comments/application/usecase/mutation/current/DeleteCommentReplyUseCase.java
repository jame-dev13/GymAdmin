package com.jame.dev.gymApp.features.comments.application.usecase.mutation.current;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;

import java.util.UUID;

public interface DeleteCommentReplyUseCase {
   void deleteCommentReply(UUID commentId, String replyId, AuthPrincipal principal);
}
