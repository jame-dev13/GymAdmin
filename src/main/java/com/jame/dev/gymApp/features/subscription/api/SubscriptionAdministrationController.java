package com.jame.dev.gymApp.features.subscription.api;

import com.jame.dev.gymApp.features.subscription.api.request.PaymentRequest;
import com.jame.dev.gymApp.features.subscription.api.request.SubscriptionRequest;
import com.jame.dev.gymApp.features.subscription.api.response.SubscriptionAboutToEndResponse;
import com.jame.dev.gymApp.features.subscription.api.response.SubscriptionResponse;
import com.jame.dev.gymApp.features.subscription.application.usecases.mutation.*;
import com.jame.dev.gymApp.features.subscription.application.usecases.query.GetByIdSubscriptionUseCase;
import com.jame.dev.gymApp.features.subscription.application.usecases.query.GetPageSubscriptionUseCase;
import com.jame.dev.gymApp.features.subscription.application.usecases.query.GetSubsAboutToEndPageUseCase;
import com.jame.dev.gymApp.features.subscription.domain.event.CompletedCheckoutEvent;
import com.jame.dev.gymApp.features.subscription.domain.model.PaymentPhysicMeta;
import com.jame.dev.gymApp.infrastructure.annotation.Minimum;
import com.jame.dev.gymApp.infrastructure.annotation.NotNullObject;
import com.jame.dev.gymApp.infrastructure.page.mapper.PageResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/app/v1/administration/subs")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@Validated
public class SubscriptionAdministrationController {

   private final GetPageSubscriptionUseCase subscriptionGetPage;
   private final GetByIdSubscriptionUseCase subscriptionGetById;
   private final CreateSubscriptionUseCase subscriptionCreate;
   private final UpdateSubscriptionUseCase subscriptionUpdate;
   private final RenewSubscriptionUseCase subscriptionRenew;
   private final FinalizeSubscriptionUseCase subscriptionFinalize;
   private final SoftDeleteSubscriptionByIdUseCase subscriptionSoftDelete;
   private final CreatePaymentUseCase createPaymentUseCase;
   private final CompletedCheckoutUseCase completedCheckoutUseCase;
   private final GetSubsAboutToEndPageUseCase subscriptionAboutToEnd;
   private final PageResponseMapper pageResponseMapper;

   @GetMapping("/about-to-end")
   public ResponseEntity<Page<SubscriptionAboutToEndResponse>> getSubsABoutToEndPage(
      @PageableDefault(
         sort = "id",
         direction = Sort.Direction.DESC) final Pageable pageable,
      @RequestParam(required = false, name = "search") final String search
   ) {
      final var page = pageResponseMapper.from(subscriptionAboutToEnd.getPage(pageable, search), pageable);
      return ResponseEntity.ok(page);
   }

   @GetMapping
   public ResponseEntity<Page<SubscriptionResponse>> getPage(
      @PageableDefault(
         sort = "id",
         direction = Sort.Direction.DESC) final Pageable pageable,
      @RequestParam(required = false, name = "search") final String search) {
      final var page = pageResponseMapper.from(subscriptionGetPage.getPage(pageable, search), pageable);
      return ResponseEntity.ok(page);
   }

   @GetMapping("/{id}")
   public ResponseEntity<SubscriptionResponse> getById(
      @PathVariable("id") @Minimum final long id) {
      final SubscriptionResponse response = subscriptionGetById.getById(id);
      return ResponseEntity.ok(response);
   }

   @PostMapping
   public ResponseEntity<SubscriptionResponse> create(
      @RequestBody @Valid @NotNullObject final SubscriptionRequest subscriptionRequest) {

      final SubscriptionResponse subscription = subscriptionCreate.create(subscriptionRequest);
      final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
         .path("/{id}")
         .buildAndExpand(subscription.id())
         .toUri();

      final CompletedCheckoutEvent checkoutEvent = new CompletedCheckoutEvent(
         UUID.randomUUID().toString(),
         PaymentPhysicMeta.PAYMENT_INTEND_ID.getValue(),
         PaymentPhysicMeta.STRIPE_SUBSCRIPTION_ID.getValue(),
         subscription.customerEmail());
      log.info("checkout event created.");

      createPaymentUseCase.create(
         PaymentRequest.builder()
            .sessionId(checkoutEvent.stripeSessionId())
            .intentId(checkoutEvent.stripePaymentIntentId())
            .subscriptionSessionId(checkoutEvent.stripeSubscriptionId())
            .isPhysical(true)
            .subscriptionId(subscription.id())
            .build()
      );

      final var checkoutResult = completedCheckoutUseCase.execute(checkoutEvent);

      return ResponseEntity.created(location)
         .body(checkoutResult.subscription());
   }

   @PutMapping("/{id}")
   public ResponseEntity<SubscriptionResponse> update(
      @PathVariable("id")
      @Minimum final long id,
      @RequestBody
      @Valid
      @NotNullObject final SubscriptionRequest subscriptionRequest) {
      final SubscriptionResponse response = subscriptionUpdate.update(id, subscriptionRequest);
      return ResponseEntity.ok(response);
   }

   @PutMapping("/{id}/renew")
   public ResponseEntity<SubscriptionResponse> renew(
      @PathVariable("id")
      @Minimum final long id,
      @RequestBody
      @Valid
      @NotNullObject final SubscriptionRequest subscriptionRequest) {
      final SubscriptionResponse response = subscriptionRenew.renew(id, subscriptionRequest);
      return ResponseEntity.ok(response);
   }

   @PatchMapping("/{id}")
   public ResponseEntity<SubscriptionResponse> finalize(
      @PathVariable("id")
      @Minimum final long id) {
      final SubscriptionResponse response = subscriptionFinalize.finalize(id);
      return ResponseEntity.ok(response);
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> delete(
      @PathVariable("id")
      @Minimum final long id) {
      subscriptionSoftDelete.softDeleteById(id);
      return ResponseEntity.noContent().build();
   }
}

