package com.jame.dev.gymApp.features.subscription.application.usecases.query;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.subscription.api.response.PaymentResponse;

public interface GetLastPaymentMadeUseCase {
   PaymentResponse getLastPaymentMade(AuthPrincipal authPrincipal);
}
