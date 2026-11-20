package com.jame.dev.gymApp.features.subscription.application.service.query;

import com.jame.dev.gymApp.application.model.CacheValues;
import com.jame.dev.gymApp.domain.exception.NotFoundException;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.subscription.api.response.PaymentResponse;
import com.jame.dev.gymApp.features.subscription.application.usecases.query.GetLastPaymentMadeUseCase;
import com.jame.dev.gymApp.features.subscription.domain.repository.PaymentQueryRepository;
import com.jame.dev.gymApp.features.subscription.infrastructure.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetLastPaymentMadeUseCaseService implements GetLastPaymentMadeUseCase {
   private final PaymentQueryRepository paymentQueryRepository;
   private final PaymentMapper paymentMapper;

   @Override
   @Cacheable(
      key = CacheValues.PAYMENTS,
      keyGenerator = "appKeyGenerator",
      unless = "#result == null"
   )
   public PaymentResponse getLastPaymentMade(AuthPrincipal authPrincipal) {
      return paymentQueryRepository.findLastPaymentByCustomerId(authPrincipal.id())
         .map(paymentMapper::toResponse)
         .orElseThrow(() -> new NotFoundException("Last made payment not found"));
   }
}
