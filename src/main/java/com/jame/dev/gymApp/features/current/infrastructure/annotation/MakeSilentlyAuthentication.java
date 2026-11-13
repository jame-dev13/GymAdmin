package com.jame.dev.gymApp.features.current.infrastructure.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MakeSilentlyAuthentication {
}
