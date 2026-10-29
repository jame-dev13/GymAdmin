package com.jame.dev.gymApp.features.comments.domain.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
@Document(collection = "comments")
public class CommentDocument {

   @MongoId
   @Builder.Default
   private final UUID id =  UUID.randomUUID();

   @Field("authorId")
   @Indexed(name = "comment_author_id_idx")
   private Long authorId;

   @Field("from")
   private String from;

   @Field("comment")
   private String comment;

   @Field("commentType")
   private CommentType commentType;

   @Field("rate")
   private Double rate;

   @Field("createdAt")
   @Indexed(direction = IndexDirection.DESCENDING, name = "comment_created_at_idx")
   @Builder.Default
   @Setter(AccessLevel.NONE)
   private final Instant createdAt = Instant.now();

   @Field("updatedAt")
   private Instant updatedAt;

   @Override
   public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;
      CommentDocument that = (CommentDocument) o;
      return Objects.equals(id, that.id);
   }

   @Override
   public int hashCode() {
      return Objects.hashCode(id);
   }
}
