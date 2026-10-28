package com.jame.dev.gymApp.features.comments.infrastructure.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface OffenseFilter {
}
