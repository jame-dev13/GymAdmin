package com.jame.dev.gymApp.features.comments.infrastructure.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Document
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Caching(
   evict = @CacheEvict(value = CommentCacheValues.REPLIES, keyGenerator = "appKeyGenerator")
)
public @interface EvictRepliesOnSave {
}
