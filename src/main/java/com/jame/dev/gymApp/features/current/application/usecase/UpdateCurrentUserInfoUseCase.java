package com.jame.dev.gymApp.features.current.application.usecase;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.current.application.model.CurrentUserModel;
import com.jame.dev.gymApp.features.current.application.request.CurrentUserUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UpdateCurrentUserInfoUseCase {
   CurrentUserModel updateCurrentUserInfo(
      final AuthPrincipal principal,
      final CurrentUserUpdateRequest currentUserUpdateRequest,
      final HttpServletRequest request,
      final HttpServletResponse response);
}
