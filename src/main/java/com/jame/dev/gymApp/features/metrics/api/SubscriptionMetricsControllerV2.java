package com.jame.dev.gymApp.features.metrics.api;

import com.jame.dev.gymApp.features.metrics.api.response.SubscriptionAnnualResumeResponseV2;
import com.jame.dev.gymApp.features.metrics.application.usecases.query.GetSubscriptionsAnnualResumeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${app.context-path.admin.v2}/metrics/subs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SubscriptionMetricsControllerV2 {

   private final GetSubscriptionsAnnualResumeUseCase resumeUseCase;

   @GetMapping("/resume")
   public ResponseEntity<SubscriptionAnnualResumeResponseV2> getAnnualResume() {
      return ResponseEntity.ok(resumeUseCase.getAnnualResume());
   }
}
