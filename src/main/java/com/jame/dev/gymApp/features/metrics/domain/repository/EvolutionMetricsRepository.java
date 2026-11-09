package com.jame.dev.gymApp.features.metrics.domain.repository;

import com.jame.dev.gymApp.features.metrics.application.model.BillingEvolutionModel;
import com.jame.dev.gymApp.features.metrics.application.model.CustomerEvolutionModel;
import com.jame.dev.gymApp.features.metrics.application.model.SubscriberEvolutionModel;
import com.jame.dev.gymApp.features.metrics.domain.model.CustomerEvolution;
import com.jame.dev.gymApp.features.metrics.domain.model.SubscriberEvolution;
import com.jame.dev.gymApp.features.metrics.domain.model.MonthTotal;

import java.util.List;

public interface EvolutionMetricsRepository {

   CustomerEvolutionModel calculateCustomerEvolution(final long year);

   SubscriberEvolutionModel calculateSubscriberEvolution(final long year);

   BillingEvolutionModel calculateBillingEvolutionInsights(final long year);

    @Deprecated
   List<CustomerEvolution> calculateJoiningCustomerEvolution(final long year);

    @Deprecated
   List<CustomerEvolution> calculateDowningCustomerEvolution(final long year);

    @Deprecated
   List<SubscriberEvolution> calculateJoiningSubscriberEvolution(final long year);

    @Deprecated
   List<SubscriberEvolution> calculateDowningSubscribersBeforeEndTime(final long year);

    @Deprecated
   List<MonthTotal> calculateBillingEvolution(final long year);
}
