package com.jame.dev.gymApp.features.backup.infrastructure.adapter;

import com.jame.dev.gymApp.domain.exception.NotFoundException;
import com.jame.dev.gymApp.features.backup.domain.model.BackupDocument;
import com.jame.dev.gymApp.features.backup.domain.repository.BackupMutationRepository;
import com.jame.dev.gymApp.features.backup.infrastructure.persistence.BackupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("mongoMutationBackupRepository")
@RequiredArgsConstructor
public class BackupMutationMongoRepositoryAdapter implements BackupMutationRepository {
   private final BackupRepository backupRepository;

   @Override
   public BackupDocument save(BackupDocument backupDocument) {
      return backupRepository.save(backupDocument);
   }

   @Override
   public void deleteBackupById(UUID uuid) {
      final BackupDocument backupDocument = findActiveById(uuid);
      backupDocument.setActive(false);
      backupRepository.save(backupDocument);
   }

   @Override
   public void hardDeleteBackupById(UUID uuid) {
      final BackupDocument backupDocument = findInactiveById(uuid);
      backupRepository.delete(backupDocument);
   }

   @Override
   public void recoverBackupById(UUID uuid) {
      final BackupDocument backupDocument = findInactiveById(uuid);
      backupDocument.setActive(true);
      backupRepository.save(backupDocument);
   }

   private BackupDocument findActiveById(final UUID uuid) {
      return backupRepository.findById(uuid)
         .filter(BackupDocument::isActive)
         .orElseThrow(() -> new NotFoundException("Backup not found for: " + uuid));
   }

   private BackupDocument findInactiveById(final UUID uuid) {
      return backupRepository.findById(uuid)
         .filter(document -> !document.isActive())
         .orElseThrow(() -> new NotFoundException("Backup not found for: " + uuid));
   }
}
