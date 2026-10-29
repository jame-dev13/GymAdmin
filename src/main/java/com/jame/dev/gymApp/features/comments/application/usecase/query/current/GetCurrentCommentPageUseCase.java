package com.jame.dev.gymApp.features.comments.application.usecase.query.current;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import org.springframework.data.domain.Pageable;

public interface GetCurrentCommentPageUseCase {
   PageDto<CommentResponse> getPage(
      final AuthPrincipal principal,
      final Pageable pageable,
      final String search
   );
}
