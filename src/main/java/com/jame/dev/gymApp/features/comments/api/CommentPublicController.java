package com.jame.dev.gymApp.features.comments.api;

import com.jame.dev.gymApp.features.comments.application.response.CommentPublicResponse;
import com.jame.dev.gymApp.features.comments.application.usecase.query.pub.GetBestRatedCommentsUseCase;
import com.jame.dev.gymApp.infrastructure.annotation.Minimum;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/public/v1/comments")
@RestController
@RequiredArgsConstructor
@Validated
public class CommentPublicController {
   private static final int MAX_LIMIT = 20;

   private final GetBestRatedCommentsUseCase getBestRatedCommentsUseCase;

   @GetMapping("/{limit}")
   public ResponseEntity<CommentPublicResponse> getBestRatedComments(
      @PathVariable("limit")
      @Minimum
      @Max(value = MAX_LIMIT, message = "Value must be less than or equal to " + MAX_LIMIT + ".")
      final int limit
   ) {
      return ResponseEntity.ok(getBestRatedCommentsUseCase.getBestRatedWithLimit(limit));
   }
}
