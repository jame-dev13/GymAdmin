package com.jame.dev.gymApp.features.audit.application.usecases;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.audit.api.response.AuditLogResponse;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import org.springframework.data.domain.Pageable;

public interface GetAuditLogPageByCurrentUseCase {
   PageDto<AuditLogResponse> getPage(
      final AuthPrincipal authPrincipal,
      final Pageable pageable,
      final String search);
}
