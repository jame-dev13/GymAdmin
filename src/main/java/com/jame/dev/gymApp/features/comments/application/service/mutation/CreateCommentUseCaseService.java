package com.jame.dev.gymApp.features.comments.application.service.mutation;

import com.jame.dev.gymApp.features.comments.application.request.CommentRequest;
import com.jame.dev.gymApp.features.comments.application.support.factory.CommentFactory;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.CreateCommentUseCase;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentMutationRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.annotation.OffenseFilter;
import com.jame.dev.gymApp.features.comments.infrastructure.cache.EvictCommentsOnCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCommentUseCaseService implements CreateCommentUseCase {
   private final CommentMutationRepository commentMutationRepository;
   private final CommentFactory commentFactory;

   @Override
   @OffenseFilter
   @EvictCommentsOnCreate
   public void create(CommentRequest commentRequest) {
      log.info("create comment use case service.");
      final var comment = commentFactory.fromRequest(commentRequest);
      commentMutationRepository.save(comment);
   }
}
