package com.jame.dev.gymApp.features.current.application.service;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.auth.domain.model.CustomOAuth2User;
import com.jame.dev.gymApp.features.auth.domain.model.UserPrincipal;
import com.jame.dev.gymApp.features.current.application.response.CurrentOverviewResponse;
import com.jame.dev.gymApp.features.current.application.usecase.GetCurrentOverviewUseCase;
import com.jame.dev.gymApp.features.current.infrastructure.cache.CurrentCacheValues;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetCurrentOverviewUseCaseService implements GetCurrentOverviewUseCase {

   @Override
   @Cacheable(
      value = CurrentCacheValues.CURRENT_OVERVIEW,
      keyGenerator = "authPrincipalCurrentKeyGen",
      unless = "#result == null"
   )
   public CurrentOverviewResponse getCurrentOverview(AuthPrincipal principal) {
      return switch (principal) {
         case UserPrincipal local -> new CurrentOverviewResponse(local.name(), local.username());
         case CustomOAuth2User oauth -> new CurrentOverviewResponse(oauth.name(), oauth.username());
      };
   }
}
