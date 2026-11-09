package com.jame.dev.gymApp.features.metrics.application.service;

import com.jame.dev.gymApp.features.metrics.api.response.BillingEvolutionResponse;
import com.jame.dev.gymApp.features.metrics.api.response.CustomerEvolutionResponse;
import com.jame.dev.gymApp.features.metrics.api.response.SubscriberEvolutionResponse;
import com.jame.dev.gymApp.features.metrics.application.contract.EvolutionMetricsService;
import com.jame.dev.gymApp.features.metrics.domain.repository.EvolutionMetricsRepository;
import com.jame.dev.gymApp.features.metrics.infrastructure.cache.CacheEvolutionMetricsValues;
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
public class EvolutionMetricsApplicationService implements EvolutionMetricsService {
   private final EvolutionMetricsRepository evolutionMetricsRepository;

   @Override
   @Cacheable(
      value = CacheEvolutionMetricsValues.CUSTOMER_EVOLUTION,
      key = "#year",
      unless = "#result == null"
   )
   public CustomerEvolutionResponse getCustomerEvolution(long year) {
      return new CustomerEvolutionResponse(evolutionMetricsRepository.calculateCustomerEvolution(year));
   }

   @Override
   @Cacheable(
      value = CacheEvolutionMetricsValues.SUBSCRIBER_EVOLUTION,
      key = "#year",
      unless = "#result == null"
   )
   public SubscriberEvolutionResponse getSubscriberEvolution(long year) {
      return new SubscriberEvolutionResponse(evolutionMetricsRepository.calculateSubscriberEvolution(year));
   }

   @Override
   @Cacheable(
      value = CacheEvolutionMetricsValues.BILLING_EVOLUTION,
      key = "#year",
      unless = "#result == null"
   )
   public BillingEvolutionResponse getBillingEvolution(long year) {
      return new BillingEvolutionResponse(evolutionMetricsRepository.calculateBillingEvolutionInsights(year));
   }
}
