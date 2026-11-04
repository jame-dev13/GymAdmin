package com.jame.dev.gymApp.features.metrics.application.service.query;

import com.jame.dev.gymApp.features.metrics.api.response.OverviewResponse;
import com.jame.dev.gymApp.features.metrics.application.usecases.query.GetOverviewUseCase;
import com.jame.dev.gymApp.features.metrics.domain.repository.OverviewRepository;
import com.jame.dev.gymApp.features.metrics.infrastructure.cache.CacheMetricValues;
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
public class GetOverviewUseCaseService implements GetOverviewUseCase {
   private final OverviewRepository overviewRepository;

   @Override
   @Cacheable(
      value = CacheMetricValues.OVERVIEW,
      keyGenerator = "appKeyGenerator",
      unless = "#result == null"
   )
   public OverviewResponse getOverview() {
      return overviewRepository.calculateOverviewMetrics();
   }
}
