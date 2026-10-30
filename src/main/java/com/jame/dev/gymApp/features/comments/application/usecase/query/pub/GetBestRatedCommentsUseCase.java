package com.jame.dev.gymApp.features.comments.application.usecase.query.pub;

import com.jame.dev.gymApp.features.comments.application.response.CommentPublicResponse;

public interface GetBestRatedCommentsUseCase {
   CommentPublicResponse getBestRatedWithLimit(final int limit);
}
