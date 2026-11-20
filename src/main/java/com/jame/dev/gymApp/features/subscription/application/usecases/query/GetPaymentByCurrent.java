package com.jame.dev.gymApp.features.subscription.application.usecases.query;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.subscription.api.response.PaymentResponse;
import org.springframework.data.domain.Pageable;

public interface GetPaymentByCurrent {
   PageDto<PaymentResponse> getPaymentByCurrent(final AuthPrincipal authPrincipal, final String search, final Pageable pageable);
}
