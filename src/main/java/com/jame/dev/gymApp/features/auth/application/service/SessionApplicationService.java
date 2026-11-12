package com.jame.dev.gymApp.features.auth.application.service;

import com.jame.dev.gymApp.features.auth.api.response.SessionResponse;
import com.jame.dev.gymApp.features.auth.application.contract.session.SessionService;
import com.jame.dev.gymApp.features.auth.application.support.factory.AuthResponsesFactory;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.auth.domain.model.CustomOAuth2User;
import com.jame.dev.gymApp.features.auth.domain.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SessionApplicationService implements SessionService {
   private final AuthResponsesFactory authResponsesFactory;

   @Override
   public SessionResponse getSession(AuthPrincipal principal) {
      final AuthPrincipal principalSafe = Objects.requireNonNull(principal, "Something went wrong: 'authentication principal' is required.");

      return switch (principalSafe) {
         case UserPrincipal localAuth ->
            authResponsesFactory.createSessionFrom(localAuth.username(), localAuth.getAuthorities());
         case CustomOAuth2User oauth -> authResponsesFactory.createSessionFrom(oauth.username(), oauth.authorities());
      };
   }
}
