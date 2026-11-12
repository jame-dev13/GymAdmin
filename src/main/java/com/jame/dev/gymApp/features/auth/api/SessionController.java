package com.jame.dev.gymApp.features.auth.api;

import com.jame.dev.gymApp.features.auth.api.response.SessionResponse;
import com.jame.dev.gymApp.features.auth.application.contract.session.SessionService;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/app/v1/session")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') || hasRole('USER')")
public class SessionController {

   private final SessionService sessionService;

   @GetMapping
   public ResponseEntity<SessionResponse> getCurrentSession(@AuthenticationPrincipal AuthPrincipal principal) {
      final SessionResponse sessionResponse = sessionService.getSession(principal);
      return ResponseEntity.ok(sessionResponse);
   }
}
