package com.jame.dev.gymApp.features.current.infrastructure.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Caching(
   evict = @CacheEvict(value = {CurrentCacheValues.CURRENT_OVERVIEW, CurrentCacheValues.CURRENT}, keyGenerator = "authPrincipalCurrentKeyGen")
)
public @interface EvictCurrentOnMutation {
}
