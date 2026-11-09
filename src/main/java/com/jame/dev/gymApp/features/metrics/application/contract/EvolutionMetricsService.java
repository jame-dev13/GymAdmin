package com.jame.dev.gymApp.features.metrics.application.contract;

import com.jame.dev.gymApp.features.metrics.api.response.BillingEvolutionResponse;
import com.jame.dev.gymApp.features.metrics.api.response.CustomerEvolutionResponse;
import com.jame.dev.gymApp.features.metrics.api.response.SubscriberEvolutionResponse;

public interface EvolutionMetricsService {
   CustomerEvolutionResponse getCustomerEvolution(final long year);

   SubscriberEvolutionResponse getSubscriberEvolution(final long year);

   BillingEvolutionResponse getBillingEvolution(final long year);
}
