package com.jame.dev.gymApp.features.backup.application.usecases;

import com.jame.dev.gymApp.application.dto.PageDto;
import com.jame.dev.gymApp.features.backup.api.response.BackupResponse;
import org.springframework.data.domain.Pageable;

public interface GetInactiveBackupPageUseCase {
   PageDto<BackupResponse> getInactiveBackupPage(final Pageable pageable, final String search);
}
