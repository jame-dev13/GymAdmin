package com.jame.dev.gymApp.features.comments.infrastructure.adapter;

import com.jame.dev.gymApp.features.comments.domain.repository.CommentValidationRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.persistence.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CommentValidationMongoRepository implements CommentValidationRepository {
   private final CommentRepository commentRepository;

   @Override
   public boolean existsById(UUID commentId) {
      return commentRepository.existsById(commentId);
   }

   @Override
   public boolean existsByIdAndAuthorId(UUID commentId, Long authorId) {
      return commentRepository.existsByIdAndAuthorId(commentId, authorId);
   }
}
