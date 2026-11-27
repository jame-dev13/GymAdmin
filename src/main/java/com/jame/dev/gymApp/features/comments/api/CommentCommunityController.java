package com.jame.dev.gymApp.features.comments.api;

import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.comments.application.response.CommunityCommentResponse;
import com.jame.dev.gymApp.features.comments.application.usecase.GetCommunityCommentPageUseCase;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/v1/community/comments")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@Validated
public class CommentCommunityController {
   private final GetCommunityCommentPageUseCase getCommunityCommentPageUseCase;
   private final PageResponseMapper pageResponseMapper;

   @GetMapping
   public ResponseEntity<Page<CommunityCommentResponse>> getCommunityCommentPage(
      @PageableDefault(sort = "creation") final Pageable pageable,
      @NotNull @AuthenticationPrincipal AuthPrincipal principal
   ) {
      final var pageDto = getCommunityCommentPageUseCase.getCommunityPage(pageable, principal);
      return ResponseEntity.ok(pageResponseMapper.from(pageDto, pageable));
   }
}
