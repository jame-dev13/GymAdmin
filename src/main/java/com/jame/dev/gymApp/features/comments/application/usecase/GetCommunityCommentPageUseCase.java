package com.jame.dev.gymApp.features.comments.application.usecase;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.response.CommunityCommentResponse;
import org.springframework.data.domain.Pageable;

public interface GetCommunityCommentPageUseCase {
   PageDto<CommunityCommentResponse> getCommunityPage(final Pageable pageable, final AuthPrincipal principal);
}
