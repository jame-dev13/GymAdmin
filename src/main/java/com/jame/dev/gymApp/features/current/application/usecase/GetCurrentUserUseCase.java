package com.jame.dev.gymApp.features.current.application.usecase;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.current.application.response.CurrentUserResponse;

public interface GetCurrentUserUseCase {
   CurrentUserResponse getCurrentUser(final AuthPrincipal authPrincipal);
}
