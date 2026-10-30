package com.jame.dev.gymApp.features.comments.infrastructure.persistence;

import com.jame.dev.gymApp.features.comments.domain.model.CommentDocument;

import java.util.Set;

public interface CommentPublicSpecification {
   Set<CommentDocument> findBestRatedLimited(final int limit);
}
