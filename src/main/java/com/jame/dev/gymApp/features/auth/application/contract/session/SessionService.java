package com.jame.dev.gymApp.features.auth.application.contract.session;

import com.jame.dev.gymApp.features.auth.api.response.SessionResponse;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;

public interface SessionService {
   SessionResponse getSession(AuthPrincipal principal);
}
