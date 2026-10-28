package com.jame.dev.gymApp.features.comments.infrastructure.persistence;

import com.jame.dev.gymApp.domain.repository.CustomMongoSpecification;
import com.jame.dev.gymApp.features.comments.domain.model.CommentDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomMongoSpecification<CommentDocument> {
   private final MongoTemplate mt;

   @Override
   public Page<CommentDocument> search(Pageable pageable, String search) {
      final Query query = new Query();
      query.with(pageable);

      if (search != null && !search.isBlank()) {
         query.addCriteria(
            new Criteria()
               .orOperator(
                  Criteria.where("from").regex(search, "i"),
                  Criteria.where("comment").regex(search, "i"),
                  Criteria.where("commentType").regex(search, "i")
               )
         );
      }

      final List<CommentDocument> content = mt.find(query, CommentDocument.class);
      final long totalElements = mt.count(
         Query.of(query)
            .skip(-1)
            .limit(-1),
         CommentDocument.class
      );
      return new PageImpl<>(content, pageable, totalElements);
   }
}
