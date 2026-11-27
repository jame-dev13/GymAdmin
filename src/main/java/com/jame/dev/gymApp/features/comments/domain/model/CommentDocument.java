package com.jame.dev.gymApp.features.comments.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
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
   private final UUID id = UUID.randomUUID();

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
   @Builder.Default
   private Double rate = 0d;

   @Field("replies")
   @JsonIgnore
   @Builder.Default
   @DBRef(lazy = true)
   private List<CommentReply> replies = new LinkedList<>();

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

   public void addReply(CommentReply reply) {
      this.replies.addLast(reply);
      this.setReplies(replies);
   }

   public boolean setReply(String replyId, CommentReply newReply) {
      final var reply = this.replies.stream()
         .filter(cr -> cr.replyId().equals(replyId))
         .findFirst()
         .orElse(null);

      if(reply == null) return false;

      final int index = this.replies.indexOf(reply);

      this.replies.set(index, newReply);

      this.setReplies(replies);
      return true;
   }

   public boolean removeReply(String replyId) {
      final var reply = this.replies.stream()
         .filter(cr -> cr.replyId().equals(replyId))
         .findFirst()
         .orElse(null);

      if(reply == null) return false;

      this.replies.remove(reply);
      this.setReplies(replies);

      return true;
   }
}
