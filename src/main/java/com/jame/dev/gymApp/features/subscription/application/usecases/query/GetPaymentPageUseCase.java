package com.jame.dev.gymApp.features.subscription.application.usecases.query;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.subscription.api.response.PaymentResponse;
import org.springframework.data.domain.Pageable;

public interface GetPaymentPageUseCase {
   PageDto<PaymentResponse> getPage(final Pageable pageable, final String search);
}
