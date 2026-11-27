package com.jame.dev.gymApp.features.comments.application.usecase.mutation.current;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.request.CommentReplyRequest;

import java.util.UUID;

public interface CreateCommentReplyUseCase {
   void createReply(UUID commentId, CommentReplyRequest commentReplyRequest, AuthPrincipal principal);
}
