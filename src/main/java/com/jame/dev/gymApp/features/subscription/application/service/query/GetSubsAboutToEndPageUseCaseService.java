package com.jame.dev.gymApp.features.subscription.application.service.query;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.application.model.CacheValues;
import com.jame.dev.gymApp.features.subscription.api.response.SubscriptionAboutToEndResponse;
import com.jame.dev.gymApp.features.subscription.application.contract.SubscriptionFactory;
import com.jame.dev.gymApp.features.subscription.application.usecases.query.GetSubsAboutToEndPageUseCase;
import com.jame.dev.gymApp.features.subscription.domain.model.SubscriptionEntity;
import com.jame.dev.gymApp.features.subscription.domain.model.SubscriptionStatus;
import com.jame.dev.gymApp.features.subscription.domain.repository.SubscriptionQueryRepository;
import com.jame.dev.gymApp.features.subscription.infrastructure.specification.SubscriptionSpecifications;
import com.jame.dev.gymApp.infrastructure.security.lock.CheckLockProcess;
import com.jame.dev.gymApp.infrastructure.security.lock.LockKeys;
import com.jame.dev.gymApp.infrastructure.sort.SortPropertyResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@CheckLockProcess(keys = {LockKeys.PG_RESTORE})
public class GetSubsAboutToEndPageUseCaseService implements GetSubsAboutToEndPageUseCase {
   private final SortPropertyResolver subSortAppResolver;
   private final SubscriptionFactory subscriptionFactory;
   private final SubscriptionQueryRepository subscriptionQueryRepository;

   private final Set<SubscriptionStatus> STATUSES = Set.of(SubscriptionStatus.PAID, SubscriptionStatus.RENEWED);

   private final Clock clock;

   @Override
   @Cacheable(
      value = CacheValues.SUBSCRIPTIONS_TO_END,
      keyGenerator = "pageKeyGenerator",
      unless = "#result == null || #result.content.isEmpty()"
   )
   public PageDto<SubscriptionAboutToEndResponse> getPage(Pageable pageable, String search) {
      final LocalDate LIMIT = LocalDate.now(clock).plusDays(7);
      final Pageable pageableValid = subSortAppResolver.resolve(pageable);
      final Specification<SubscriptionEntity> specification = Specification
         .where(SubscriptionSpecifications.hasEndPeriodBetween(LocalDate.now(clock), LIMIT)
            .and(SubscriptionSpecifications.hasStatuses(STATUSES))
            .and(SubscriptionSpecifications.containsSearch(search))
         );

      final var page = subscriptionQueryRepository.findAllAboutToEnd(specification, pageableValid);

      return subscriptionFactory.fromPage(page);
   }
}
