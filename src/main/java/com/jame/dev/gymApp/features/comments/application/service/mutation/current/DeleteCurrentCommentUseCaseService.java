package com.jame.dev.gymApp.features.comments.application.service.mutation.current;

import com.jame.dev.gymApp.domain.exception.NotFoundException;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.current.DeleteCurrentCommentUseCase;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentMutationRepository;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentQueryRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.cache.EvictCurrentCommentsOnDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteCurrentCommentUseCaseService implements DeleteCurrentCommentUseCase {
   private final CommentQueryRepository commentQueryRepository;
   private final CommentMutationRepository commentMutationRepository;

   @Override
   @EvictCurrentCommentsOnDelete
   public void delete(final AuthPrincipal principal, final UUID commentId) {
      final var comment = commentQueryRepository.findByIdAndAuthorId(commentId, principal.id())
         .orElseThrow(() -> new NotFoundException("Comment not found."));

      commentMutationRepository.deleteById(comment.getId());
   }
}
