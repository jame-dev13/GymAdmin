package com.jame.dev.gymApp.features.audit.application.service;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.audit.api.response.AuditLogResponse;
import com.jame.dev.gymApp.features.audit.application.support.factory.AuditLogFactory;
import com.jame.dev.gymApp.features.audit.application.usecases.GetAuditLogPageByCurrentUseCase;
import com.jame.dev.gymApp.features.audit.domain.model.AuditLogDocument;
import com.jame.dev.gymApp.features.audit.infrastructure.cache.AuditCacheValues;
import com.jame.dev.gymApp.features.audit.infrastructure.specification.AuditLogSpecifications;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.infrastructure.sort.SortPropertyResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAuditLogPageByCurrentUCService implements GetAuditLogPageByCurrentUseCase {
   private final AuditLogSpecifications auditLogSpecifications;
   private final AuditLogFactory auditLogFactory;
   private final SortPropertyResolver auditLogSortAppResolver;

   @Override
   @Cacheable(
      value = AuditCacheValues.AUDIT_LOG_CURR_VAL,
      keyGenerator = "pageKeyGenerator",
      unless = "#result == null || #result.content.isEmpty()"
   )
   public PageDto<AuditLogResponse> getPage(AuthPrincipal principal, Pageable pageable, String search) {
      final var pageableWrapped = auditLogSortAppResolver.resolve(pageable);
      final Page<AuditLogDocument> logoDocuementPage = auditLogSpecifications.findAllByCurrentActor(principal, pageableWrapped, search);
      return auditLogFactory.createPageFrom(logoDocuementPage);
   }
}
