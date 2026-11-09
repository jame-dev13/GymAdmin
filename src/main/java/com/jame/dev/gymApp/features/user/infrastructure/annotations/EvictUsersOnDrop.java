package com.jame.dev.gymApp.features.user.infrastructure.annotations;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.jame.dev.gymApp.application.model.CacheValues.*;
import static com.jame.dev.gymApp.features.metrics.infrastructure.cache.CacheEvolutionMetricsValues.CUSTOMER_EVOLUTION;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(evict = {
   @CacheEvict(value = {USERS, CUSTOMERS, CUSTOMER_EVOLUTION}, allEntries = true),
   @CacheEvict(value = USER, key = "#id")
})
public @interface EvictUsersOnDrop {
}
