package com.jame.dev.gymApp.features.metrics.application.service.query;

import com.jame.dev.gymApp.features.metrics.api.response.SubscriptionAnnualResumeResponseV2;
import com.jame.dev.gymApp.features.metrics.application.usecases.query.GetSubscriptionsAnnualResumeUseCase;
import com.jame.dev.gymApp.features.metrics.domain.repository.SubscriptionMetricsRepositoryV2;
import com.jame.dev.gymApp.features.metrics.infrastructure.cache.CacheMetricValues;
import com.jame.dev.gymApp.infrastructure.security.lock.CheckLockProcess;
import com.jame.dev.gymApp.infrastructure.security.lock.LockKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@CheckLockProcess(keys = {LockKeys.PG_RESTORE})
@Transactional(readOnly = true)
public class GetSubscriptionsAnnualResumeUseCaseService implements GetSubscriptionsAnnualResumeUseCase {
   private final SubscriptionMetricsRepositoryV2 v2;

   @Override
   @Cacheable(
      value = CacheMetricValues.SUBSCRIPTIONS,
      keyGenerator = "appKeyGenerator",
      unless = "#result == null"
   )
   public SubscriptionAnnualResumeResponseV2 getAnnualResume() {
      return new SubscriptionAnnualResumeResponseV2(v2.calculateAnnualResume());
   }
}
