package com.jame.dev.gymApp.features.comments.application.support.mapper;

import com.jame.dev.gymApp.features.comments.application.model.CommentPublic;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import com.jame.dev.gymApp.features.comments.domain.model.CommentDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

   @Mapping(target = "commentId", source = "id")
   @Mapping(target = "editedAt", source = "updatedAt")
   CommentResponse toResponse(CommentDocument commentDocument);

   @Mapping(target = "editedAt", source = "updatedAt")
   CommentPublic toPublicResponse(CommentDocument commentDocument);
}
