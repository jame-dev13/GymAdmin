package com.jame.dev.gymApp.features.subscription.api;

import com.jame.dev.gymApp.features.subscription.api.response.PaymentResponse;
import com.jame.dev.gymApp.features.subscription.application.usecases.query.GetPaymentPageUseCase;
import com.jame.dev.gymApp.infrastructure.page.mapper.PageResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/app/v1/administration/payments")
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class PaymentAdminController {
   private final GetPaymentPageUseCase getPaymentPageUseCase;
   private final PageResponseMapper pageResponseMapper;

   @GetMapping
   public ResponseEntity<Page<PaymentResponse>> getPage(
      @PageableDefault(sort = "createdAt", direction = Sort.Direction.ASC) final Pageable pageable,
      @RequestParam(value = "search", required = false) final String search
   ) {
      final var body = pageResponseMapper.from(getPaymentPageUseCase.getPage(pageable, search), pageable);
      return ResponseEntity.ok(body);
   }
}
