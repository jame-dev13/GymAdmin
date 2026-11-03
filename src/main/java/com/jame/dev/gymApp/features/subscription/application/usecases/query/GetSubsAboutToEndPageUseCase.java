package com.jame.dev.gymApp.features.subscription.application.usecases.query;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.subscription.api.response.SubscriptionAboutToEndResponse;
import org.springframework.data.domain.Pageable;

public interface GetSubsAboutToEndPageUseCase {
   PageDto<SubscriptionAboutToEndResponse> getPage(final Pageable pageable, final String search);
}
