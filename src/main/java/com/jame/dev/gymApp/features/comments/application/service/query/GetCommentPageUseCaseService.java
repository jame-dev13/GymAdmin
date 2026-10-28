package com.jame.dev.gymApp.features.comments.application.service.query;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import com.jame.dev.gymApp.features.comments.application.support.factory.CommentFactory;
import com.jame.dev.gymApp.features.comments.application.usecase.query.GetCommentPageUseCase;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentQueryRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.cache.CommentCacheValues;
import com.jame.dev.gymApp.infrastructure.sort.SortPropertyResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCommentPageUseCaseService implements GetCommentPageUseCase {
   private final CommentQueryRepository commentQueryRepository;
   private final CommentFactory commentFactory;
   private final SortPropertyResolver commentSortPropertyResolver;

   @Override
   @Cacheable(
      value = CommentCacheValues.COMMENTS,
      keyGenerator = "pageKeyGenerator",
      unless = "#result == null || #result.content.isEmpty()"
   )
   public PageDto<CommentResponse> getPage(Pageable pageable, String search) {
      final var commentPage = commentQueryRepository.findAll(commentSortPropertyResolver.resolve(pageable), search);
      return commentFactory.fromPage(commentPage);
   }
}
