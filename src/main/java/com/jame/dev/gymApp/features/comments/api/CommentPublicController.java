package com.jame.dev.gymApp.features.comments.api;

import com.jame.dev.gymApp.features.comments.application.response.CommentPublicResponse;
import com.jame.dev.gymApp.features.comments.application.usecase.query.pub.GetBestRatedCommentsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/public/v1/comments")
@RestController
@RequiredArgsConstructor
public class CommentPublicController {
   private final GetBestRatedCommentsUseCase getBestRatedCommentsUseCase;

   @GetMapping("/{limit}")
   public ResponseEntity<CommentPublicResponse> getBestRatedComments(
      @PathVariable("limit") int limit
   ) {
      return ResponseEntity.ok(getBestRatedCommentsUseCase.getBestRatedWithLimit(limit));
   }
}
