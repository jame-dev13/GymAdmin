package com.jame.dev.gymApp.features.comments.application.support.factory;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.comments.application.request.CommentRequest;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import com.jame.dev.gymApp.features.comments.application.support.mapper.CommentMapper;
import com.jame.dev.gymApp.features.comments.domain.model.CommentDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentFactory {
   private final CommentMapper commentMapper;

   public PageDto<CommentResponse> fromPage(final Page<CommentDocument> commentsPage) {
      return PageDto.<CommentResponse>builder()
         .content(commentsPage.getContent().stream().map(commentMapper::toResponse).toList())
         .page(commentsPage.getNumber())
         .size(commentsPage.getSize())
         .totalElements(commentsPage.getTotalElements())
         .sortDirection(commentsPage.getSort().isSorted() ? "ASC" : "DESC")
         .sortProperty(commentsPage.getSort().toString())
         .build();
   }

   public CommentResponse fromDocument(CommentDocument commentDocument) {
      return commentMapper.toResponse(commentDocument);
   }

   public CommentDocument fromRequest(CommentRequest commentRequest) {
      return CommentDocument.builder()
         .from(commentRequest.from())
         .comment(commentRequest.comment())
         .commentType(commentRequest.commentType())
         .rate(commentRequest.rate())
         .build();
   }

}
