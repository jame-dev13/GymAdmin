package com.jame.dev.gymApp.features.metrics.domain.repository;

import com.jame.dev.gymApp.features.metrics.application.model.SubscriptionAnnualResumeModel;

public interface SubscriptionMetricsRepositoryV2 {
   SubscriptionAnnualResumeModel calculateAnnualResume();
}
