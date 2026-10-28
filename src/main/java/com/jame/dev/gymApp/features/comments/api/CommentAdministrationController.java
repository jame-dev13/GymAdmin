package com.jame.dev.gymApp.features.comments.api;

import com.jame.dev.gymApp.features.comments.application.request.CommentRequest;
import com.jame.dev.gymApp.features.comments.application.response.CommentResponse;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.CreateCommentUseCase;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.DeleteCommentUseCase;
import com.jame.dev.gymApp.features.comments.application.usecase.mutation.UpdateCommentUseCase;
import com.jame.dev.gymApp.features.comments.application.usecase.query.GetCommentByIdUseCase;
import com.jame.dev.gymApp.features.comments.application.usecase.query.GetCommentPageUseCase;
import com.jame.dev.gymApp.infrastructure.page.mapper.PageResponseMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/app/v1/administration/comments")
@Validated
@RequiredArgsConstructor
public class CommentAdministrationController {
   private final GetCommentPageUseCase getCommentPageUseCase;
   private final GetCommentByIdUseCase getCommentByIdUseCase;
   private final CreateCommentUseCase createCommentUseCase;
   private final UpdateCommentUseCase updateCommentUseCase;
   private final DeleteCommentUseCase deleteCommentUseCase;
   private final PageResponseMapper pageResponseMapper;

   @GetMapping("/{id}")
   public ResponseEntity<CommentResponse> getCommentPage(@PathVariable("id") UUID commentId) {
      return ResponseEntity.ok(getCommentByIdUseCase.getById(commentId));
   }

   @GetMapping
   public ResponseEntity<Page<CommentResponse>> getCommentPage(
      @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.ASC) final Pageable pageable,
      @RequestParam(required = false, value = "search") final String search
   ) {
      final var pageDto = getCommentPageUseCase.getPage(pageable, search);
      return ResponseEntity.ok(pageResponseMapper.from(pageDto, pageable));
   }

   @PostMapping
   public ResponseEntity<Void> makeComment(@RequestBody @Valid CommentRequest commentRequest) {
      createCommentUseCase.create(commentRequest);
      return ResponseEntity.ok().build();
   }

   @PutMapping("/{id}")
   public ResponseEntity<Void> editComment(
      @PathVariable("id") final UUID commentId,
      @RequestBody CommentRequest commentRequest) {
      updateCommentUseCase.update(commentId, commentRequest);
      return ResponseEntity.ok().build();
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteComment(
      @PathVariable("id") UUID commentId) {
      deleteCommentUseCase.delete(commentId);
      return ResponseEntity.ok().build();
   }

}

