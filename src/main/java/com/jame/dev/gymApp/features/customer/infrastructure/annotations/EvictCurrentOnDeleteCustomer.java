package com.jame.dev.gymApp.features.customer.infrastructure.annotations;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.jame.dev.gymApp.application.model.CacheValues.*;
import static com.jame.dev.gymApp.features.metrics.infrastructure.cache.CacheEvolutionMetricsValues.DOWNING_CUSTOMERS;
import static com.jame.dev.gymApp.features.metrics.infrastructure.cache.CacheMetricValues.OVERVIEW;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(evict = {
   @CacheEvict(value = {CUSTOMERS, DOWNING_CUSTOMERS, OVERVIEW, SUBSCRIPTIONS}, allEntries = true),
   @CacheEvict(value = CUSTOMER, keyGenerator = "authPrincipalCurrentKeyGen")
})
public @interface EvictCurrentOnDeleteCustomer {
}
