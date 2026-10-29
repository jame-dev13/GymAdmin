package com.jame.dev.gymApp.features.comments.application.service.query.current;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import com.jame.dev.gymApp.features.comments.application.support.factory.CommentFactory;
import com.jame.dev.gymApp.features.comments.application.usecase.query.current.GetCurrentCommentPageUseCase;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentQueryRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.cache.CommentCacheValues;
import com.jame.dev.gymApp.infrastructure.sort.SortPropertyResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCurrentCommentPageUseCaseService implements GetCurrentCommentPageUseCase {
   private final CommentQueryRepository commentQueryRepository;
   private final CommentFactory commentFactory;
   private final SortPropertyResolver commentSortPropertyResolver;

   @Override
   @Cacheable(
      value = CommentCacheValues.CURRENT_COMMENTS,
      keyGenerator = "commentCurrentPageKeyGenerator",
      unless = "#result == null || #result.content.isEmpty()"
   )
   public PageDto<CommentResponse> getPage(
      final AuthPrincipal principal,
      final Pageable pageable,
      final String search
   ) {
      final var commentPage = commentQueryRepository.findAllByAuthorId(
         principal.id(),
         commentSortPropertyResolver.resolve(pageable),
         search
      );
      return commentFactory.fromPage(commentPage);
   }
}
