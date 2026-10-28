package com.jame.dev.gymApp.features.comments.application.usecase.query;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import org.springframework.data.domain.Pageable;

public interface GetCommentPageUseCase {
   PageDto<CommentResponse> getPage(final Pageable pageable, final String search);
}
