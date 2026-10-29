package com.jame.dev.gymApp.features.comments.application.service.mutation.current;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.request.CommentRequest;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import com.jame.dev.gymApp.features.comments.application.support.factory.CommentFactory;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.current.CreateCurrentCommentUseCase;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentMutationRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.annotation.OffenseFilter;
import com.jame.dev.gymApp.features.comments.infrastructure.cache.EvictCurrentCommentsOnCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCurrentCommentUseCaseService implements CreateCurrentCommentUseCase {
   private final CommentMutationRepository commentMutationRepository;
   private final CommentFactory commentFactory;

   @Override
   @OffenseFilter
   @EvictCurrentCommentsOnCreate
   public CommentResponse create(final AuthPrincipal principal, final CommentRequest commentRequest) {
      log.info("create current comment use case service.");
      final var comment = commentFactory.fromRequest(commentRequest, principal.id());
      final var commentSaved = commentMutationRepository.save(comment);
      return commentFactory.fromDocument(commentSaved);
   }
}
