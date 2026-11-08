package com.jame.dev.gymApp.features.subscription.application.service.query;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.application.model.CacheValues;
import com.jame.dev.gymApp.features.subscription.api.response.PaymentResponse;
import com.jame.dev.gymApp.features.subscription.application.support.factory.PaymentFactory;
import com.jame.dev.gymApp.features.subscription.application.usecases.query.GetPaymentPageUseCase;
import com.jame.dev.gymApp.features.subscription.domain.repository.PaymentQueryRepository;
import com.jame.dev.gymApp.features.subscription.infrastructure.specification.PaymentSpecifications;
import com.jame.dev.gymApp.infrastructure.security.lock.CheckLockProcess;
import com.jame.dev.gymApp.infrastructure.security.lock.LockKeys;
import com.jame.dev.gymApp.infrastructure.sort.SortPropertyResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@CheckLockProcess(keys = {LockKeys.PG_RESTORE})
public class GetPaymentPageUseCaseService implements GetPaymentPageUseCase {
   private final PaymentQueryRepository paymentQueryRepository;
   private final SortPropertyResolver paymentSortResolver;
   private final PaymentFactory paymentFactory;

   @Override
   @Cacheable(
      value = CacheValues.PAYMENTS,
      keyGenerator = "pageKeyGenerator",
      unless = "#result == null || #result.content.isEmpty()"
   )
   public PageDto<PaymentResponse> getPage(Pageable pageable, String search) {
      final var pageableValid = paymentSortResolver.resolve(pageable);
      final var specification = PaymentSpecifications.hasSearchIn(search);
      final var page = paymentQueryRepository.finaPage(specification, pageableValid);
      return paymentFactory.fromPage(page);
   }
}
