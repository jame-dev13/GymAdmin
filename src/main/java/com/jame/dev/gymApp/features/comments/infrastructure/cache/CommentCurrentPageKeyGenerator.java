package com.jame.dev.gymApp.features.comments.infrastructure.cache;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component("commentCurrentPageKeyGenerator")
public class CommentCurrentPageKeyGenerator implements KeyGenerator {

   @Override
   public Object generate(final Object target, final Method method, final Object... params) {
      final Long authorId = Arrays.stream(params)
         .filter(AuthPrincipal.class::isInstance)
         .map(AuthPrincipal.class::cast)
         .findFirst()
         .map(AuthPrincipal::id)
         .orElseThrow(() -> new IllegalArgumentException("Auth Principal param not found."));

      final String pageKey = Arrays.stream(params)
         .filter(Pageable.class::isInstance)
         .map(Pageable.class::cast)
         .findFirst()
         .map(CommentCurrentPageKeyGenerator::pageKey)
         .orElse("");

      final String search = Arrays.stream(params)
         .filter(String.class::isInstance)
         .map(String.class::cast)
         .findFirst()
         .orElse("");

      return "author:" + authorId + "|" + pageKey + "|str:" + search;
   }

   private static String pageKey(final Pageable pageable) {
      return "page:" + pageable.getPageNumber()
         + ":" + pageable.getPageSize()
         + ":sort=" + pageable.getSort();
   }
}
