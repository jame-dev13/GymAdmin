package com.jame.dev.gymApp.features.metrics.domain.repository;

import com.jame.dev.gymApp.features.metrics.api.response.OverviewResponse;

public interface OverviewRepository {
   OverviewResponse calculateOverviewMetrics();
}
