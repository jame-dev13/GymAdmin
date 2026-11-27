package com.jame.dev.gymApp.features.comments.api;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.request.CommentReplyRequest;
import com.jame.dev.gymApp.features.comments.application.request.CommentRequest;
import com.jame.dev.gymApp.features.comments.application.response.CommentReplyResponse;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.current.*;
import com.jame.dev.gymApp.features.comments.application.usecase.query.GetCommentReplyPageUseCase;
import com.jame.dev.gymApp.features.comments.application.usecase.query.current.GetCurrentCommentPageUseCase;
import com.jame.dev.gymApp.infrastructure.annotation.NotNullObject;
import com.jame.dev.gymApp.infrastructure.page.mapper.PageResponseMapper;
import com.jame.dev.gymApp.infrastructure.page.mapper.SliceResponseMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
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
   private final GetCommentReplyPageUseCase getCommentReplyPageUseCase;
   private final CreateCommentReplyUseCase createCommentReplyUseCase;
   private final EditCommentReplyUseCase editCommentReplyUseCase;
   private final DeleteCommentReplyUseCase deleteCommentReplyUseCase;
   private final PageResponseMapper pageResponseMapper;
   private final SliceResponseMapper sliceResponseMapper;

   @GetMapping
   public ResponseEntity<Page<CommentResponse>> getPage(
      @AuthenticationPrincipal final AuthPrincipal principal,
      @PageableDefault final Pageable pageable,
      @RequestParam(required = false, value = "search") final String search
   ) {
      final var pageDto = getCurrentCommentPageUseCase.getPage(principal, pageable, search);
      return ResponseEntity.ok(pageResponseMapper.from(pageDto, pageable));
   }


   @GetMapping("/{commentId}/replies")
   public ResponseEntity<Slice<CommentReplyResponse>> getCommentReplies(
      @NotNull @PathVariable("commentId") UUID commentId,
      @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
   ) {
      final var sliceDto = getCommentReplyPageUseCase.getCommentReplyPage(commentId, pageable);
      return ResponseEntity.ok(sliceResponseMapper.from(sliceDto, pageable));
   }

   @PostMapping("/{commentId}/replies")
   public ResponseEntity<Void> makeReply(
      @Valid
      @RequestBody
      @NotNull CommentReplyRequest commentReplyRequest,
      @NotNull
      @AuthenticationPrincipal AuthPrincipal principal,
      @PathVariable("commentId")
      @NotNull UUID commentId
   ) {
      createCommentReplyUseCase.createReply(commentId, commentReplyRequest, principal);
      return ResponseEntity.ok().build();
   }

   @PutMapping("/{commentId}/replies/{replyId}")
   public ResponseEntity<Void> editReply(
      @NotNull @PathVariable("commentId") UUID commentId,
      @NotBlank @PathVariable("replyId") String replyId,
      @RequestBody @NotNull @Valid final CommentReplyRequest commentReplyRequest,
      @NotNull @AuthenticationPrincipal AuthPrincipal principal
   ) {
      editCommentReplyUseCase.editCommentReply(commentId, replyId, commentReplyRequest, principal);
      return ResponseEntity.ok().build();
   }

   @DeleteMapping("/{commentId}/replies/{replyId}")
   public ResponseEntity<Void> removeReply(
      @NotNull @PathVariable("commentId") UUID commentId,
      @NotBlank @PathVariable("replyId") String replyId,
      @NotNull @AuthenticationPrincipal AuthPrincipal principal
   ) {
      deleteCommentReplyUseCase.deleteCommentReply(commentId, replyId, principal);
      return ResponseEntity.noContent().build();
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
