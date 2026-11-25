package com.jame.dev.gymApp.features.audit.infrastructure.specification;

import com.jame.dev.gymApp.features.audit.domain.model.AuditLogDocument;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogSpecifications {
   Page<AuditLogDocument> findAllByCurrentActor(
      final AuthPrincipal authPrincipal, final Pageable pageable, final String search
   );
}
