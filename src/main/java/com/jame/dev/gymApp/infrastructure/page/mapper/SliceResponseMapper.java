package com.jame.dev.gymApp.infrastructure.page.mapper;

import com.jame.dev.gymApp.application.dto.SliceDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Component;

@Component
public class SliceResponseMapper {

   public final <T> Slice<T> from(final SliceDto<T> sliceDto, final Pageable pageable) {
      return new SliceImpl<T>(sliceDto.content(), pageable, sliceDto.hasNext());
   }
}
