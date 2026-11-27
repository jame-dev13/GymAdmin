package com.jame.dev.gymApp.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record SliceDto<T> (
        @JsonProperty("content") List<T> content,
        @JsonProperty("page") int page,
        @JsonProperty("size") int size,
        @JsonProperty("hasNext") boolean hasNext,
        @JsonProperty("hasPrevious") boolean hasPrevious,
        @JsonProperty("sortProperty") String sortProperty,
        @JsonProperty("sortDirection") String sortDirection
) {
}
