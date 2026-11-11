package com.jame.dev.gymApp.features.backup.infrastructure.persistence;

import com.jame.dev.gymApp.domain.repository.CustomMongoSpecification;
import com.jame.dev.gymApp.features.backup.domain.model.BackupDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BackupSearchRepository extends CustomMongoSpecification<BackupDocument> {
   Page<BackupDocument> searchInactive(final Pageable pageable, final String search);
}
