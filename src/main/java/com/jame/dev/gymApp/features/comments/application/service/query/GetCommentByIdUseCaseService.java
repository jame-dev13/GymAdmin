package com.jame.dev.gymApp.features.comments.application.service.query;

import com.jame.dev.gymApp.domain.exception.NotFoundException;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import com.jame.dev.gymApp.features.comments.application.support.mapper.CommentMapper;
import com.jame.dev.gymApp.features.comments.application.usecase.query.GetCommentByIdUseCase;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentQueryRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.cache.CommentCacheValues;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCommentByIdUseCaseService implements GetCommentByIdUseCase {
   private final CommentQueryRepository commentQueryRepository;
   private final CommentMapper commentMapper;

   @Override
   @Cacheable(
      value = CommentCacheValues.COMMENT,
      key = "#commentId",
      unless = "#result == null"
   )
   public CommentResponse getById(UUID commentId) {
      return commentQueryRepository.findById(commentId)
         .map(commentMapper::toResponse)
         .orElseThrow(() -> new NotFoundException("Comment not found."));
   }
}
