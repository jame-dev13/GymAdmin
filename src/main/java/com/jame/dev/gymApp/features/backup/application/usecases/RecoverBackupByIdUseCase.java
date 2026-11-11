package com.jame.dev.gymApp.features.backup.application.usecases;

import java.util.UUID;

public interface RecoverBackupByIdUseCase {
   void recoverById(final UUID uuid);
}
