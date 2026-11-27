package com.jame.dev.gymApp.features.comments.application.service.query;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.response.CommunityCommentResponse;
import com.jame.dev.gymApp.features.comments.application.support.factory.CommentFactory;
import com.jame.dev.gymApp.features.comments.application.usecase.GetCommunityCommentPageUseCase;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentQueryRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.cache.CommentCacheValues;
import com.jame.dev.gymApp.infrastructure.sort.SortPropertyResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCommunityCommentPageUseCaseService implements GetCommunityCommentPageUseCase {
   private final CommentQueryRepository queryRepository;
   private final CommentFactory commentFactory;
   private final SortPropertyResolver commentSortPropertyResolver;

   @Override
   @Cacheable(
      value = CommentCacheValues.COMMENTS,
      keyGenerator = "pageKeyGenerator",
      unless = "#result == null || #result.content.isEmpty()"
   )
   public PageDto<CommunityCommentResponse> getCommunityPage(Pageable pageable, AuthPrincipal principal) {
      final var documentPage = queryRepository.findAll(commentSortPropertyResolver.resolve(pageable));
      return commentFactory.fromPage(documentPage, principal);
   }
}
