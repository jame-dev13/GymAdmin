package com.jame.dev.gymApp.features.comments.infrastructure.cache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Caching(
   evict = {
      @CacheEvict(cacheNames = {CommentCacheValues.COMMENTS}, keyGenerator = "pageKeyGenerator", allEntries = true),
      @CacheEvict(cacheNames = {CommentCacheValues.COMMENT}, key = "#commentId"),
      @CacheEvict(cacheNames = {CommentCacheValues.CURRENT_COMMENTS}, allEntries = true),
   }
)
public @interface EvictCurrentCommentsOnDelete {
}
