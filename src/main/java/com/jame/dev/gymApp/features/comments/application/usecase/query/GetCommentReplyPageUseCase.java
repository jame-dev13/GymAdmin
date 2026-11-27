package com.jame.dev.gymApp.features.comments.application.usecase.query;

import com.jame.dev.gymApp.application.dto.SliceDto;
import com.jame.dev.gymApp.features.comments.application.response.CommentReplyResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GetCommentReplyPageUseCase {
   SliceDto<CommentReplyResponse> getCommentReplyPage(UUID commentId, Pageable pageable);
}
