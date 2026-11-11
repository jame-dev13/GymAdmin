package com.jame.dev.gymApp.features.backup.application.usecases;

import java.util.UUID;

public interface HardDeleteBackupByIdUseCase {
   void hardDeleteById(final UUID uuid);
}
