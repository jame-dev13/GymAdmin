package com.jame.dev.gymApp.features.comments.infrastructure.sort;

import com.jame.dev.gymApp.infrastructure.sort.SortPropertyResolver;
import com.jame.dev.gymApp.infrastructure.sort.SortResolver;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Map;
import java.util.stream.Collectors;

@Component("commentSortPropertyResolver")
public class CommentSortPropertyResolver implements SortPropertyResolver {
   final Map<String, String> PROPERTY_MAP = EnumSet.allOf(CommentSortProperties.class)
      .stream()
      .collect(Collectors.toUnmodifiableMap(
         CommentSortProperties::getExposedProperty,
         CommentSortProperties::getAppProperty
      ));

   @Override
   public Pageable resolve(Pageable pageable) {
      return SortResolver.resolveSortPropertiesFrom(pageable, PROPERTY_MAP);
   }
}
