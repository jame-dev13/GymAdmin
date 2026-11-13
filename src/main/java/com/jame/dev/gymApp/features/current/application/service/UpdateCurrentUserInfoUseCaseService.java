package com.jame.dev.gymApp.features.current.application.service;

import com.jame.dev.gymApp.domain.exception.NotFoundException;
import com.jame.dev.gymApp.features.auth.domain.exception.AlreadyExistsException;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.current.application.model.CurrentUserModel;
import com.jame.dev.gymApp.features.current.application.request.CurrentUserUpdateRequest;
import com.jame.dev.gymApp.features.current.application.usecase.UpdateCurrentUserInfoUseCase;
import com.jame.dev.gymApp.features.current.infrastructure.cache.EvictCurrentOnMutation;
import com.jame.dev.gymApp.features.user.domain.model.UserEntity;
import com.jame.dev.gymApp.features.user.domain.repository.UserMutationRepository;
import com.jame.dev.gymApp.features.user.domain.repository.UserQueryRepository;
import com.jame.dev.gymApp.features.user.domain.repository.UserValidationRepository;
import com.jame.dev.gymApp.infrastructure.security.lock.CheckLockProcess;
import com.jame.dev.gymApp.infrastructure.security.lock.LockKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@CheckLockProcess(keys = {LockKeys.PG_DUMP, LockKeys.PG_RESTORE})
public class UpdateCurrentUserInfoUseCaseService implements UpdateCurrentUserInfoUseCase {
   private final UserQueryRepository userQueryRepository;
   private final UserValidationRepository userValidationRepository;
   private final UserMutationRepository userMutationRepository;
   private final PasswordEncoder passwordEncoder;

   @Override
   @Transactional
   @EvictCurrentOnMutation
   public CurrentUserModel updateCurrentUserInfo(
      AuthPrincipal principal,
      CurrentUserUpdateRequest currentUserUpdateRequest,
      HttpServletRequest request,
      HttpServletResponse response
   ) {
      final UserEntity userEntity = userQueryRepository.findById(principal.id())
         .orElseThrow(() -> new NotFoundException("Current User not found."));

      final boolean emailChanged = !userEntity.getEmail().equals(currentUserUpdateRequest.email());
      if (emailChanged && userValidationRepository.existsByEmail(currentUserUpdateRequest.email()))
         throw new AlreadyExistsException("Cannot accept email duplicates.");

      userEntity.setName(currentUserUpdateRequest.name());
      userEntity.setEmail(currentUserUpdateRequest.email());
      if (currentUserUpdateRequest.password() != null && !currentUserUpdateRequest.password().isBlank())
         userEntity.setPassword(passwordEncoder.encode(currentUserUpdateRequest.password()));

      final UserEntity user = userMutationRepository.save(userEntity);

      // The aspect needs the raw password to silently re-authenticate the user;
      // the entity only holds the encoded one.
      return new CurrentUserModel(user.getName(), user.getEmail(), currentUserUpdateRequest.password());
   }
}
