package com.jame.dev.gymApp.features.comments.application.service.mutation.current;

import com.jame.dev.gymApp.domain.exception.NotFoundException;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.request.CommentReplyRequest;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.current.EditCommentReplyUseCase;
import com.jame.dev.gymApp.features.comments.domain.model.CommentReply;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentMutationRepository;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentQueryRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.annotation.OffenseFilter;
import com.jame.dev.gymApp.features.comments.infrastructure.cache.EvictRepliesOnSave;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EditCommentReplyUseCaseService implements EditCommentReplyUseCase {
   private final CommentQueryRepository queryRepository;
   private final CommentMutationRepository mutationRepository;

   @Override
   @OffenseFilter
   @EvictRepliesOnSave
   public void editCommentReply(UUID commentId, String replyId, CommentReplyRequest commentReplyRequest, AuthPrincipal principal) {
      final var comment = queryRepository.findByIdAndAuthorId(commentId, principal.id())
         .orElseThrow(() -> new NotFoundException("Comment not found."));

      final boolean isSettled = comment.setReply(replyId, CommentReply.builder()
         .replyId(replyId)
         .from(principal.username())
         .replyText(commentReplyRequest.comment())
         .build());

      if (!isSettled) {
         throw new UnsupportedOperationException("Could not edit comment reply");
      }

      mutationRepository.save(comment);
   }
}
