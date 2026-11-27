package com.jame.dev.gymApp.features.comments.infrastructure.persistence;

import com.jame.dev.gymApp.domain.repository.CustomMongoSpecification;
import com.jame.dev.gymApp.features.comments.domain.model.CommentDocument;
import com.jame.dev.gymApp.features.comments.domain.model.CommentReply;
import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface CommentRepository
   extends
   MongoRepository<CommentDocument, UUID>,
   CustomMongoSpecification<CommentDocument>,
   CommentAuthorSpecification,
   CommentPublicSpecification {

   Optional<CommentDocument> findByIdAndAuthorId(final UUID commentId, final Long authorId);

   boolean existsByIdAndAuthorId(final UUID commentId, final Long authorId);

   @Aggregation(pipeline = {
      "{ $match: { id: ?0, authorId: ?1 } }",
      "{ $unwind: '$replies' }",
      "{ $match: { replies: { $ne: null } } }",
      """
         {
            $facet: {
               replies: [
                  { $sort: { 'replies.createdAt': -1 } },
                  { $skip: ?2 },
                  { $limit: ?3 },
                  { $replaceRoot: { newRoot: '$replies' } }
               ],
               count: [ $count: 'total' ]
            }
         }
         """})
   Document findRepliesFor(UUID commentId, Long authorId, long offset, long limit);

   @Aggregation(pipeline = {
      "{ $match: { id: ?0 } }",
      "{ $unwind: '$replies' }",
      "{ $match: { replies: { $ne: null } } }",
      "{ $replaceRoot: { newRoot: '$replies' } }"
   })
   Slice<CommentReply> findRepliesForComment(UUID commentId, Pageable pageable);

}
