package com.jame.dev.gymApp.features.subscription.application.support.factory;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.subscription.api.response.PaymentResponse;
import com.jame.dev.gymApp.features.subscription.domain.event.StripeSessionPaymentEvent;
import com.jame.dev.gymApp.features.subscription.domain.model.PaymentEntity;
import com.jame.dev.gymApp.features.subscription.domain.model.PaymentMethod;
import com.jame.dev.gymApp.features.subscription.infrastructure.payment.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFactory {
   private final PaymentMapper paymentMapper;

   public PaymentEntity from(StripeSessionPaymentEvent paymentEvent) {
      return PaymentEntity.builder()
         .stripeSessionId(paymentEvent.sessionId())
         .stripePaymentIntentId(paymentEvent.intentId())
         .stripeSubscriptionId(paymentEvent.subscriptionId())
         .amount(paymentEvent.subscriptionEntity().getMembership().getPrice())
         .currency("mx")
         .status(paymentEvent.paymentStatus())
         .paymentMethod(paymentEvent.isPhysicSession() ? PaymentMethod.PHYSIC : PaymentMethod.ELECTRONIC)
         .subscription(paymentEvent.subscriptionEntity())
         .customer(paymentEvent.subscriptionEntity().getCustomer())
         .build();
   }

   public PageDto<PaymentResponse> fromPage(Page<PaymentEntity> page) {
      return PageDto.<PaymentResponse>builder()
         .content(page.getContent()
            .stream()
            .map(paymentMapper::toResponse)
            .toList())
         .page(page.getNumber())
         .size(page.getSize())
         .totalElements(page.getTotalElements())
         .sortProperty(page.getSort().toString())
         .sortDirection(page.getSort().isSorted() ? "asc" : "desc")
         .build();
   }
}
