package com.jame.dev.gymApp.features.comments.application.service.mutation;

import com.jame.dev.gymApp.features.comments.application.usecase.mutation.DeleteCommentUseCase;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentMutationRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.cache.EvictCommentsOnDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteCommentUseCaseService implements DeleteCommentUseCase {
   private final CommentMutationRepository commentMutationRepository;

   @Override
   @EvictCommentsOnDelete
   public void delete(UUID commentId) {
      commentMutationRepository.deleteById(commentId);
   }
}
