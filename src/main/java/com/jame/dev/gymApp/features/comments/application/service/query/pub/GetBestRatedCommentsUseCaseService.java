package com.jame.dev.gymApp.features.comments.application.service.query.pub;

import com.jame.dev.gymApp.features.comments.application.response.CommentPublicResponse;
import com.jame.dev.gymApp.features.comments.application.support.mapper.CommentMapper;
import com.jame.dev.gymApp.features.comments.application.usecase.query.pub.GetBestRatedCommentsUseCase;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetBestRatedCommentsUseCaseService implements GetBestRatedCommentsUseCase {
   private final CommentQueryRepository commentQueryRepository;
   private final CommentMapper commentMapper;

   @Override
   @Cacheable(
      value = "comments:public",
      key = "#limit",
      unless = "#result == null || #result.content.isEmpty()"
   )
   public CommentPublicResponse getBestRatedWithLimit(int limit) {
      return new CommentPublicResponse(
         commentQueryRepository
            .findBestRated(limit)
            .stream()
            .map(commentMapper::toPublicResponse)
            .collect(Collectors.toSet()));
   }
}
