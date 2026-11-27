package com.jame.dev.gymApp.features.comments.application.service.query;

import com.jame.dev.gymApp.application.dto.SliceDto;
import com.jame.dev.gymApp.features.comments.application.response.CommentReplyResponse;
import com.jame.dev.gymApp.features.comments.application.support.mapper.CommentMapper;
import com.jame.dev.gymApp.features.comments.application.usecase.query.GetCommentReplyPageUseCase;
import com.jame.dev.gymApp.features.comments.domain.model.CommentReply;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentQueryRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.cache.CommentCacheValues;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCommentReplyPageUseCaseService implements GetCommentReplyPageUseCase {
   private final CommentQueryRepository queryRepository;
   private final CommentMapper commentMapper;

   @Override
   @Cacheable(
      value = CommentCacheValues.REPLIES,
      keyGenerator = "pageKeyGenerator",
      unless = "#result == null || #result.content.isEmpty()"
   )
   public SliceDto<CommentReplyResponse> getCommentReplyPage(UUID commentId, Pageable pageable) {
      final Slice<CommentReply> commentReplies = queryRepository.findAllRepliesFor(commentId, pageable);
      final List<CommentReplyResponse> content = commentReplies.getContent()
         .stream()
         .map(commentMapper::replyToResponse)
         .toList();
      return new SliceDto<>(
         content,
         pageable.getPageNumber(),
         pageable.getPageSize(),
         commentReplies.hasNext(),
         commentReplies.hasPrevious(),
         pageable.getSort().toString(),
         pageable.getSort().isSorted() ? "ASC" : "DESC"
      );
   }
}
