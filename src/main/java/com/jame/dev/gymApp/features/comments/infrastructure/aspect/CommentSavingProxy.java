package com.jame.dev.gymApp.features.comments.infrastructure.aspect;

import com.jame.dev.gymApp.features.comments.application.request.CommentRequest;
import com.jame.dev.gymApp.features.comments.infrastructure.filter.OffenseTextFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CommentSavingProxy {

   @Lazy
   private final OffenseTextFilter offenseTextFilter;

   @Around("@annotation(com.jame.dev.gymApp.features.comments.infrastructure.annotation.OffenseFilter)")
   public Object interceptCommentSavingFlow(ProceedingJoinPoint pj) throws Throwable {
      log.info("Comment Saving proxy reached.");
      try {
         final CommentRequest commentRequest = Arrays.stream(pj.getArgs())
            .filter(CommentRequest.class::isInstance)
            .map(CommentRequest.class::cast)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Comment request parameter not found."));

         offenseTextFilter.applyFilter(commentRequest.comment());
         return pj.proceed();
      } catch (Throwable th) {
         log.error("{}", th.getMessage(), th);
         throw th;
      }
   }
}
