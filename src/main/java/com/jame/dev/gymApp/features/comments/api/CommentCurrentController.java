package com.jame.dev.gymApp.features.comments.api;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.request.CommentRequest;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.current.CreateCurrentCommentUseCase;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.current.DeleteCurrentCommentUseCase;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.current.UpdateCurrentCommentUseCase;
import com.jame.dev.gymApp.features.comments.application.usecase.query.current.GetCurrentCommentPageUseCase;
import com.jame.dev.gymApp.infrastructure.annotation.NotNullObject;
import com.jame.dev.gymApp.infrastructure.page.mapper.PageResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/app/v1/comments/current")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor
@Validated
public class CommentCurrentController {
   private final GetCurrentCommentPageUseCase getCurrentCommentPageUseCase;
   private final CreateCurrentCommentUseCase createCurrentCommentUseCase;
   private final UpdateCurrentCommentUseCase updateCurrentCommentUseCase;
   private final DeleteCurrentCommentUseCase deleteCurrentCommentUseCase;
   private final PageResponseMapper pageResponseMapper;

   @GetMapping
   public ResponseEntity<Page<CommentResponse>> getPage(
      @AuthenticationPrincipal final AuthPrincipal principal,
      @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) final Pageable pageable,
      @RequestParam(required = false, value = "search") final String search
   ) {
      final var pageDto = getCurrentCommentPageUseCase.getPage(principal, pageable, search);
      return ResponseEntity.ok(pageResponseMapper.from(pageDto, pageable));
   }

   @PostMapping
   public ResponseEntity<Void> create(
      @AuthenticationPrincipal final AuthPrincipal principal,
      @RequestBody @Valid @NotNullObject final CommentRequest commentRequest
   ) {
      final CommentResponse commentResponse = createCurrentCommentUseCase.create(principal, commentRequest);
      if (commentResponse == null) return ResponseEntity.status(HttpStatus.CONFLICT).build();
      return ResponseEntity
         .status(HttpStatus.CREATED)
         .build();
   }

   @PutMapping("/{id}")
   public ResponseEntity<Void> update(
      @AuthenticationPrincipal final AuthPrincipal principal,
      @PathVariable("id") final UUID commentId,
      @RequestBody @Valid @NotNullObject final CommentRequest commentRequest
   ) {
      final CommentResponse commentResponse = updateCurrentCommentUseCase.update(principal, commentId, commentRequest);
      if (commentResponse == null) return ResponseEntity.status(HttpStatus.CONFLICT).build();
      return ResponseEntity.ok().build();
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> delete(
      @AuthenticationPrincipal final AuthPrincipal principal,
      @PathVariable("id") final UUID commentId
   ) {
      deleteCurrentCommentUseCase.delete(principal, commentId);
      return ResponseEntity.noContent().build();
   }
}
