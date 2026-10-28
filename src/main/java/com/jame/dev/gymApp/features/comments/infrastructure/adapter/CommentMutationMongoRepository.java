package com.jame.dev.gymApp.features.comments.infrastructure.adapter;

import com.jame.dev.gymApp.features.comments.domain.model.CommentDocument;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentMutationRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.persistence.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CommentMutationMongoRepository implements CommentMutationRepository {
   private final CommentRepository commentRepository;

   @Override
   public CommentDocument save(CommentDocument commentDocument) {
      return commentRepository.save(commentDocument);
   }

   @Override
   public void deleteById(UUID commentId) {
      commentRepository.deleteById(commentId);
   }
}
