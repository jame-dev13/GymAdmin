package com.jame.dev.gymApp.features.comments.domain.repository;

import com.jame.dev.gymApp.features.comments.domain.model.CommentDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface CommentQueryRepository {
   Page<CommentDocument> findAll(final Pageable pageable, final String search);

   Page<CommentDocument> findAllByAuthorId(final Long authorId, final Pageable pageable, final String search);

   Optional<CommentDocument> findById(final UUID commentId);

   Optional<CommentDocument> findByIdAndAuthorId(final UUID commentId, final Long authorId);

   Set<CommentDocument> findBestRated(final int limit);
}
