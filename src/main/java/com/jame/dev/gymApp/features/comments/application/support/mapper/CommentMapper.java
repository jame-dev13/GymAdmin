package com.jame.dev.gymApp.features.comments.application.support.mapper;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.model.CommentPublic;
import com.jame.dev.gymApp.features.comments.application.response.CommentReplyResponse;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import com.jame.dev.gymApp.features.comments.application.response.CommunityCommentResponse;
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

   @Mapping(target = "commentId", source = "commentDocument.id")
   @Mapping(target = "editedAt", source = "commentDocument.updatedAt")
   @Mapping(target = "hasReplies", expression = "java(mapHasReplies(commentDocument))")
   @Mapping(target = "isOwner", expression = "java(mapIsOwner(commentDocument, principal))")
   CommunityCommentResponse toCommunityCommentResponse(CommentDocument commentDocument, AuthPrincipal principal);

   default boolean mapHasReplies(CommentDocument commentDocument) {
      return !commentDocument.getReplies().isEmpty();
   }

   default boolean mapIsOwner(CommentDocument commentDocument, AuthPrincipal principal) {
      return commentDocument.getAuthorId().longValue() == principal.id();
   }
}
