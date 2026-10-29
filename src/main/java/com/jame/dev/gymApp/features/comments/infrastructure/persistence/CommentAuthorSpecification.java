package com.jame.dev.gymApp.features.comments.infrastructure.persistence;

import com.jame.dev.gymApp.features.comments.domain.model.CommentDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentAuthorSpecification {
   Page<CommentDocument> searchByAuthor(final Long authorId, final Pageable pageable, final String search);
}
