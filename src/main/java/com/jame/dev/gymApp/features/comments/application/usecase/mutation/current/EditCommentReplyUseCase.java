package com.jame.dev.gymApp.features.comments.application.usecase.mutation.current;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.request.CommentReplyRequest;

import java.util.UUID;

public interface EditCommentReplyUseCase {
   void editCommentReply(UUID commentId, String replyId, CommentReplyRequest commentReplyRequest, AuthPrincipal principal);

}
