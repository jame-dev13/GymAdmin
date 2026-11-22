package com.jame.dev.gymApp.features.subscription.application.service.validation;

import com.jame.dev.gymApp.application.model.CacheValues;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.subscription.api.response.SubscriptionExistsResponse;
import com.jame.dev.gymApp.features.subscription.application.usecases.validation.ExistSubscriptionByCustomerIdUseCase;
import com.jame.dev.gymApp.features.subscription.domain.repository.SubscriptionValidationRepository;
import com.jame.dev.gymApp.infrastructure.security.lock.CheckLockProcess;
import com.jame.dev.gymApp.infrastructure.security.lock.LockKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@CheckLockProcess(keys = {LockKeys.PG_RESTORE})
public class ExistSubscriptionByCustomerIdUseCaseService implements ExistSubscriptionByCustomerIdUseCase {
   private final SubscriptionValidationRepository subscriptionValidationRepository;

   @Override
   @Cacheable(
      value = CacheValues.SUBSCRIPTION,
      keyGenerator = "appKeyGenerator",
      unless = "#result == null"
   )
   public SubscriptionExistsResponse existByCustomerId(AuthPrincipal authPrincipal) {
      return new SubscriptionExistsResponse(subscriptionValidationRepository.existsByCustomerId(authPrincipal.id()));
   }
}
