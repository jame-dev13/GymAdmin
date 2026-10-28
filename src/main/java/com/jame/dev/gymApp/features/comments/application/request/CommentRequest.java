package com.jame.dev.gymApp.features.comments.application.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jame.dev.gymApp.features.comments.domain.model.CommentType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record CommentRequest(
   @JsonProperty("from")
   @NotEmpty(message = "'From' subject is required.") String from,
   @JsonProperty("comment")
   @NotEmpty(message = "Comment is required.") String comment,
   @JsonProperty("commentType") CommentType commentType,
   @JsonProperty("rate")
   @DecimalMin(value = "0.0", message = "'Rate' must be greater than or equal to 0.")
   @DecimalMax(value = "1.0", message = "'Rate' must be less than or equal to 1.")
   Double rate
) {
}
