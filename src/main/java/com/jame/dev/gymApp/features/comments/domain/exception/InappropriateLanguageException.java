package com.jame.dev.gymApp.features.comments.domain.exception;

public class InappropriateLanguageException extends RuntimeException {
   public InappropriateLanguageException(String message) {
      super(message);
   }
}
