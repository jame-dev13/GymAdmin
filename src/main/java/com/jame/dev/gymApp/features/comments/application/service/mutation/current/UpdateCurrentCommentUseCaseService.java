package com.jame.dev.gymApp.features.comments.application.service.mutation.current;

import com.jame.dev.gymApp.domain.exception.NotFoundException;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.request.CommentRequest;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import com.jame.dev.gymApp.features.comments.application.support.factory.CommentFactory;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.current.UpdateCurrentCommentUseCase;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentMutationRepository;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentQueryRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.annotation.OffenseFilter;
import com.jame.dev.gymApp.features.comments.infrastructure.cache.EvictCurrentCommentOnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateCurrentCommentUseCaseService implements UpdateCurrentCommentUseCase {
   private final CommentQueryRepository commentQueryRepository;
   private final CommentMutationRepository commentMutationRepository;
   private final CommentFactory commentFactory;

   @Override
   @OffenseFilter
   @EvictCurrentCommentOnUpdate
   public CommentResponse update(
      final AuthPrincipal principal,
      final UUID commentId,
      final CommentRequest commentRequest
   ) {
      final var comment = commentQueryRepository.findByIdAndAuthorId(commentId, principal.id())
         .orElseThrow(() -> new NotFoundException("Comment not found."));

      comment.setFrom(commentRequest.from());
      comment.setComment(commentRequest.comment());
      comment.setCommentType(commentRequest.commentType());
      if (commentRequest.rate() != null) {
         comment.setRate(commentRequest.rate());
      }
      comment.setUpdatedAt(Instant.now());

      final var commentUpdated = commentMutationRepository.save(comment);
      return commentFactory.fromDocument(commentUpdated);
   }
}
