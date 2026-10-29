package com.jame.dev.gymApp.features.comments.infrastructure.persistence;

import com.jame.dev.gymApp.domain.repository.CustomMongoSpecification;
import com.jame.dev.gymApp.features.comments.domain.model.CommentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository
   extends
   MongoRepository<CommentDocument, UUID>,
   CustomMongoSpecification<CommentDocument>,
   CommentAuthorSpecification {

   Optional<CommentDocument> findByIdAndAuthorId(final UUID commentId, final Long authorId);
}
