package com.jame.dev.gymApp.features.comments.infrastructure.adapter;

import com.jame.dev.gymApp.features.comments.domain.model.CommentDocument;
import com.jame.dev.gymApp.features.comments.domain.repository.CommentQueryRepository;
import com.jame.dev.gymApp.features.comments.infrastructure.persistence.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CommentQueryMongoRepository implements CommentQueryRepository {
   private final CommentRepository commentRepository;


   @Override
   public Page<CommentDocument> findAll(Pageable pageable, String search) {
      return commentRepository.search(pageable, search);
   }

   @Override
   public Page<CommentDocument> findAllByAuthorId(Long authorId, Pageable pageable, String search) {
      return commentRepository.searchByAuthor(authorId, pageable, search);
   }

   @Override
   public Optional<CommentDocument> findById(UUID commentId) {
      return commentRepository.findById(commentId);
   }

   @Override
   public Optional<CommentDocument> findByIdAndAuthorId(UUID commentId, Long authorId) {
      return commentRepository.findByIdAndAuthorId(commentId, authorId);
   }
}
