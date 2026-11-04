package com.jame.dev.gymApp.features.metrics.api;

import com.jame.dev.gymApp.features.metrics.api.response.OverviewResponse;
import com.jame.dev.gymApp.features.metrics.application.usecases.query.GetOverviewUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/v1/administration/overview")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class OverviewController {
   private final GetOverviewUseCase getOverviewUseCase;

   @GetMapping
   public ResponseEntity<OverviewResponse> getOverview() {
      return ResponseEntity.ok(getOverviewUseCase.getOverview());
   }
}
