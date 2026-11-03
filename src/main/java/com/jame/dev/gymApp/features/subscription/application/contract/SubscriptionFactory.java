package com.jame.dev.gymApp.features.subscription.application.contract;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.application.support.factories.Factory;
import com.jame.dev.gymApp.features.subscription.api.response.SubscriptionAboutToEndResponse;
import com.jame.dev.gymApp.features.subscription.api.response.SubscriptionResponse;
import com.jame.dev.gymApp.features.subscription.application.dto.SubscriptionFactoryDtoInput;
import com.jame.dev.gymApp.features.subscription.domain.model.SubscriptionEntity;
import org.springframework.data.domain.Page;

public interface SubscriptionFactory extends Factory<
   SubscriptionEntity, SubscriptionResponse, SubscriptionFactoryDtoInput> {
   PageDto<SubscriptionAboutToEndResponse> fromPage(Page<SubscriptionEntity> page);
}
