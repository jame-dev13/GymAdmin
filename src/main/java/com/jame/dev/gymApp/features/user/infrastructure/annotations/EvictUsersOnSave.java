package com.jame.dev.gymApp.features.user.infrastructure.annotations;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

import static com.jame.dev.gymApp.application.model.CacheValues.USERS;
import static com.jame.dev.gymApp.features.metrics.infrastructure.cache.CacheMetricValues.OVERVIEW;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Caching(evict = {
   @CacheEvict(value = {USERS, OVERVIEW}, allEntries = true)
})
public @interface EvictUsersOnSave {
}
