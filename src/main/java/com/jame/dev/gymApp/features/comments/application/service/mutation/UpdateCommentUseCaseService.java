package com.jame.dev.gymApp.features.comments.application.service.mutation;

import com.jame.dev.gymApp.domain.exception.NotFoundException;
import com.jame.dev.gymApp.features.comments.application.request.CommentRequest;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.UpdateCommentUseCase;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentMutationRepository;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentQueryRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.annotation.OffenseFilter;
import com.jame.dev.gymApp.features.comments.infrastructure.cache.EvictCommentOnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateCommentUseCaseService implements UpdateCommentUseCase {
   private final CommentQueryRepository commentQueryRepository;
   private final CommentMutationRepository commentMutationRepository;

   @Override
   @OffenseFilter
   @EvictCommentOnUpdate
   public void update(UUID commentId, CommentRequest commentRequest) {
      final var comment = commentQueryRepository.findById(commentId)
         .orElseThrow(() -> new NotFoundException("Comment not found."));

      comment.setFrom(commentRequest.from());
      comment.setComment(commentRequest.comment());
      comment.setCommentType(commentRequest.commentType());
      if (commentRequest.rate() != null) {
         comment.setRate(commentRequest.rate());
      }
      comment.setUpdatedAt(Instant.now());

      commentMutationRepository.save(comment);
   }
}
