package com.jame.dev.gymApp.features.subscription.infrastructure.annotations;

import com.jame.dev.gymApp.features.metrics.infrastructure.cache.CacheEvolutionMetricsValues;
import com.jame.dev.gymApp.features.metrics.infrastructure.cache.CacheMetricValues;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.jame.dev.gymApp.application.model.CacheValues.SUBSCRIPTIONS;
import static com.jame.dev.gymApp.application.model.CacheValues.SUBSCRIPTIONS_TO_END;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(evict = {
   @CacheEvict(
      value = {SUBSCRIPTIONS, CacheMetricValues.SUBSCRIPTIONS, CacheEvolutionMetricsValues.JOINING_SUBSCRIBERS, SUBSCRIPTIONS_TO_END}, allEntries = true),
})
public @interface EvictSubsOnSave {
}
