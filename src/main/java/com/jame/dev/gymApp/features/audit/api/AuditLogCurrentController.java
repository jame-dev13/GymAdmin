package com.jame.dev.gymApp.features.audit.api;

import com.jame.dev.gymApp.features.audit.api.response.AuditLogResponse;
import com.jame.dev.gymApp.features.audit.application.usecases.GetAuditLogPageByCurrentUseCase;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.infrastructure.page.mapper.PageResponseMapper;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/v1/logs/current")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
@Validated
public class AuditLogCurrentController {

   private final GetAuditLogPageByCurrentUseCase getAuditLogPageByCurrentUseCase;
   private final PageResponseMapper pageResponseMapper;

   @GetMapping
   public ResponseEntity<Page<AuditLogResponse>> getCurrentLogTrace(
      @AuthenticationPrincipal
      @NotNull(message = "Authentication required, not found.") final AuthPrincipal authentication,
      @PageableDefault final Pageable pageable,
      @RequestParam(name = "search", required = false) final String search
   ) {
      return ResponseEntity.ok(
         pageResponseMapper.from(
            getAuditLogPageByCurrentUseCase.getPage(authentication, pageable, search),
            pageable
         )
      );
   }
}
