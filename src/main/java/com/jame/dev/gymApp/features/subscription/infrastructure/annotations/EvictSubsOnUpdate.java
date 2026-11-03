package com.jame.dev.gymApp.features.subscription.infrastructure.annotations;

import com.jame.dev.gymApp.features.metrics.infrastructure.cache.CacheMetricValues;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.jame.dev.gymApp.application.model.CacheValues.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(evict = {
   @CacheEvict(value = {SUBSCRIPTIONS, CacheMetricValues.EARNINGS, SUBSCRIPTIONS_TO_END}, allEntries = true),
   @CacheEvict(value = SUBSCRIPTION, key = "#id")
})
public @interface EvictSubsOnUpdate {
}
