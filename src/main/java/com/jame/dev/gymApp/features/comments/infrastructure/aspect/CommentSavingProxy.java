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

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CommentSavingProxy {

   @Lazy
   private final OffenseTextFilter offenseTextFilter;

   @Around(
      value = """
         @annotation(com.jame.dev.gymApp.features.comments.infrastructure.annotation.OffenseFilter)
         && args(commentRequest)
         """,
      argNames = "pj,commentRequest"
   )
   public Object interceptCommentSavingFlow(ProceedingJoinPoint pj, CommentRequest commentRequest) throws Throwable {
      log.info("Comment Saving proxy reached.");
      try {
         offenseTextFilter.applyFilter(commentRequest.comment());
         return pj.proceed();
      } catch (Throwable th) {
         log.error("{}", th.getMessage(), th);
         throw th;
      }
   }
}
