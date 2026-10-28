package com.jame.dev.gymApp.infrastructure.config.web;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Optional;

@Configuration
@Lazy
public class OpenAIConfig {
   @Bean
   public OpenAIClient openAIClient (@Value("${app.openai.api-key}") final String apiKey) {
      return OpenAIOkHttpClient.builder()
         .apiKey(Optional.of(apiKey).orElse(""))
         .build();
   }
   
}
