package com.jame.dev.gymApp.features.comments.infrastructure.filter;

import com.jame.dev.gymApp.features.comments.domain.exception.InappropriateLanguageException;
import com.openai.client.OpenAIClient;
import com.openai.models.moderations.Moderation;
import com.openai.models.moderations.ModerationCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Slf4j
@Component
@RequiredArgsConstructor
public class OffenseTextFilter {

   private final OpenAIClient openAIClient;

   public void applyFilter(final String content) {
      log.info("Evaluating offense filter.");
      final var params = ModerationCreateParams.builder()
         .model("omni-moderation-latest")
         .input(content)
         .build();

      final var moderation = openAIClient.moderations()
         .create(params);

      final boolean isFlagged = moderation.results()
         .stream()
         .anyMatch(Moderation::flagged);

      if (isFlagged) {
         throw new InappropriateLanguageException("You had tried to make a comment containing some words which incites hate, harassment or other bad related topics.");
      }
   }
}
