package com.jame.dev.gymApp.features.comments.application.support.mapper;

import com.jame.dev.gymApp.features.comments.application.model.CommentPublic;
import com.jame.dev.gymApp.features.comments.application.response.CommentReplyResponse;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import com.jame.dev.gymApp.features.comments.domain.model.CommentDocument;
import com.jame.dev.gymApp.features.comments.domain.model.CommentReply;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

   @Mapping(target = "commentId", source = "id")
   @Mapping(target = "editedAt", source = "updatedAt")
   @Mapping(target = "hasReplies", expression = "java(mapHasReplies(commentDocument))")
   CommentResponse toResponse(CommentDocument commentDocument);

   CommentReplyResponse replyToResponse(CommentReply reply);

   @Mapping(target = "editedAt", source = "updatedAt")
   CommentPublic toPublicResponse(CommentDocument commentDocument);

   default boolean mapHasReplies(CommentDocument commentDocument) {
      return !commentDocument.getReplies().isEmpty();
   }
}
