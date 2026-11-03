package com.jame.dev.gymApp.features.subscription.application.support.factory;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.application.support.factories.PageDtoFactory;
import com.jame.dev.gymApp.features.subscription.api.response.SubscriptionAboutToEndResponse;
import com.jame.dev.gymApp.features.subscription.api.response.SubscriptionResponse;
import com.jame.dev.gymApp.features.subscription.application.contract.SubscriptionFactory;
import com.jame.dev.gymApp.features.subscription.application.dto.SubscriptionFactoryDtoInput;
import com.jame.dev.gymApp.features.subscription.application.support.mapper.SubscriptionMapper;
import com.jame.dev.gymApp.features.subscription.domain.model.PeriodEntity;
import com.jame.dev.gymApp.features.subscription.domain.model.SubscriptionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionApplicationFactory implements SubscriptionFactory {
   private final SubscriptionMapper subscriptionMapper;
   private final PeriodFactory periodFactory;
   private final PageDtoFactory<SubscriptionEntity, SubscriptionResponse> pageSubscriptionFactory;

   @Override
   public PageDto<SubscriptionResponse> createPageFrom(final Page<SubscriptionEntity> page) {
      return pageSubscriptionFactory.createPageDtoFrom(page);
   }

   @Override
   public SubscriptionResponse createFromEntity(SubscriptionEntity entity) {
      return subscriptionMapper.toDto(entity);
   }

   @Override
   public SubscriptionEntity createFromInput(SubscriptionFactoryDtoInput input) {
      final List<PeriodEntity> periods = periodFactory.createPeriodsFrom(input.membership());
      return subscriptionMapper.toEntity(input.customer(), input.membership(), periods);
   }

   @Override
   public PageDto<SubscriptionAboutToEndResponse> fromPage(Page<SubscriptionEntity> page) {
      final var content = page.getContent().stream().map(subscriptionMapper::toSubAboutToEnd)
         .toList();
      return PageDto.<SubscriptionAboutToEndResponse>builder()
         .content(content)
         .page(page.getNumber())
         .size(page.getSize())
         .totalElements(page.getTotalElements())
         .sortProperty(page.getSort().toString())
         .sortDirection(page.getSort().isSorted() ? "asc" : "desc")
         .build();
   }
}
