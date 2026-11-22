package com.jame.dev.gymApp.features.subscription.application.usecases.validation;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.subscription.api.response.SubscriptionExistsResponse;

public interface ExistSubscriptionByCustomerIdUseCase {
   SubscriptionExistsResponse existByCustomerId(final AuthPrincipal authPrincipal);
}
