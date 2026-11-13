package com.jame.dev.gymApp.features.current.api;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.current.application.request.CurrentUserUpdateRequest;
import com.jame.dev.gymApp.features.current.application.response.CurrentOverviewResponse;
import com.jame.dev.gymApp.features.current.application.response.CurrentUserResponse;
import com.jame.dev.gymApp.features.current.application.usecase.GetCurrentOverviewUseCase;
import com.jame.dev.gymApp.features.current.application.usecase.GetCurrentUserUseCase;
import com.jame.dev.gymApp.features.current.application.usecase.UpdateCurrentUserInfoUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("${app.context-path.v1}/current")
@RestController
@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
@RequiredArgsConstructor
@Validated
public class CurrentController {
   private final GetCurrentOverviewUseCase currentOverviewUseCase;
   private final GetCurrentUserUseCase getCurrentUserUseCase;
   private final UpdateCurrentUserInfoUseCase updateCurrentUserInfoUseCase;

   private static final String AUTH_NULL_MESSAGE = "Authentication is required.";
   private static final String BODY_NULL_MESSAGE = "Request body is required";

   @GetMapping
   public ResponseEntity<CurrentOverviewResponse> getCurrent(
      @AuthenticationPrincipal
      @NotNull(message = AUTH_NULL_MESSAGE) final AuthPrincipal principal) {
      return ResponseEntity.ok(currentOverviewUseCase.getCurrentOverview(principal));
   }

   @GetMapping("/user")
   public ResponseEntity<CurrentUserResponse> getCurrentUser(
      @AuthenticationPrincipal
      @NotNull(message = AUTH_NULL_MESSAGE) final AuthPrincipal authPrincipal) {
      return ResponseEntity.ok(
         getCurrentUserUseCase.getCurrentUser(authPrincipal)
      );
   }

   @PatchMapping("/user")
   public ResponseEntity<Void> updateCurrentUser(
      @Valid
      @AuthenticationPrincipal
      @NotNull(message = AUTH_NULL_MESSAGE) final AuthPrincipal authPrincipal,
      @RequestBody
      @NotNull(message = BODY_NULL_MESSAGE) final CurrentUserUpdateRequest currentUserUpdateRequest,
      final HttpServletRequest request,
      final HttpServletResponse response
   ) {
      updateCurrentUserInfoUseCase.updateCurrentUserInfo(
         authPrincipal,
         currentUserUpdateRequest,
         request,
         response
      );
      return ResponseEntity.ok().build();
   }
}
