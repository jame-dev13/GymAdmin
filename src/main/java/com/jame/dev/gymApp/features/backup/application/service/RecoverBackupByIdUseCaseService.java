package com.jame.dev.gymApp.features.backup.application.service;

import com.jame.dev.gymApp.features.backup.application.usecases.RecoverBackupByIdUseCase;
import com.jame.dev.gymApp.features.backup.domain.repository.BackupMutationRepository;
import com.jame.dev.gymApp.features.backup.infrastructure.annotations.EvictBackups;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecoverBackupByIdUseCaseService implements RecoverBackupByIdUseCase {
   private final BackupMutationRepository backupMutationRepository;

   @Override
   @EvictBackups
   public void recoverById(UUID uuid) {
      backupMutationRepository.recoverBackupById(uuid);
   }
}
