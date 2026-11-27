package com.jame.dev.gymApp.features.comments.application.service.mutation.current;

import com.jame.dev.gymApp.domain.exception.NotFoundException;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.current.DeleteCommentReplyUseCase;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentMutationRepository;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteCommentReplyUseCaseService implements DeleteCommentReplyUseCase {
   private final CommentQueryRepository queryRepository;
   private final CommentMutationRepository mutationRepository;

   @Override
   public void deleteCommentReply(UUID commentId, String replyId, AuthPrincipal principal) {
      final var comment = queryRepository.findByIdAndAuthorId(commentId, principal.id())
         .orElseThrow(() -> new NotFoundException("Comment not found."));

      final boolean removed = comment.removeReply(replyId);

      if (!removed) {
         throw new UnsupportedOperationException("Could not remove reply.");
      }

      mutationRepository.save(comment);
   }
}
