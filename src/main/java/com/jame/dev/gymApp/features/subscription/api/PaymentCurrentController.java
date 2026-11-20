package com.jame.dev.gymApp.features.subscription.api;


import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.subscription.api.response.PaymentResponse;
import com.jame.dev.gymApp.features.subscription.api.response.RetryResponse;
import com.jame.dev.gymApp.features.subscription.application.support.handler.RetryPaymentHandler;
import com.jame.dev.gymApp.features.subscription.application.usecases.query.GetPaymentByCurrent;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/v1/subscribers/payments/current")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
@Validated
public class PaymentCurrentController {

   private final GetPaymentByCurrent getPaymentByCurrent;
   private final RetryPaymentHandler retryPaymentHandler;

   private static final String AUTHENTICATION_PRINCIPAL_NULL_MSG = "Authentication is required";

   @GetMapping
   public ResponseEntity<Page<PaymentResponse>> getPaymentPageByCustomerId(
      @AuthenticationPrincipal
      @NotNull(message = AUTHENTICATION_PRINCIPAL_NULL_MSG)
      final AuthPrincipal authentication,
      @RequestParam(name = "search", required = false)
      final String search,
      @PageableDefault(sort = "id", direction = Sort.Direction.DESC)
      final Pageable pageable) {
      final var pageDto = getPaymentByCurrent.getPaymentByCurrent(authentication, search, pageable);
      final Page<PaymentResponse> page = new PageImpl<>(pageDto.content(), pageable, pageDto.totalElements());
      return ResponseEntity.ok(page);
   }

   @PostMapping("/retry")
   public ResponseEntity<RetryResponse> retryPayment(
      @AuthenticationPrincipal
      @NotNull(message = AUTHENTICATION_PRINCIPAL_NULL_MSG)
      final AuthPrincipal principal) {
      return retryPaymentHandler.handleSubscriptionPaymentRetry(principal);
   }
}
