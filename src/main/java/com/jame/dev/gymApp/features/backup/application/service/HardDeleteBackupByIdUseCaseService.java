package com.jame.dev.gymApp.features.backup.application.service;

import com.jame.dev.gymApp.features.backup.application.usecases.HardDeleteBackupByIdUseCase;
import com.jame.dev.gymApp.features.backup.domain.repository.BackupMutationRepository;
import com.jame.dev.gymApp.features.backup.infrastructure.annotations.EvictBackups;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HardDeleteBackupByIdUseCaseService implements HardDeleteBackupByIdUseCase {
   private final BackupMutationRepository backupMutationRepository;

   @Override
   @EvictBackups
   public void hardDeleteById(UUID uuid) {
      backupMutationRepository.hardDeleteBackupById(uuid);
   }
}
