package com.jame.dev.gymApp.features.current.application.service;

import com.jame.dev.gymApp.domain.exception.NotFoundException;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.current.application.response.CurrentUserResponse;
import com.jame.dev.gymApp.features.current.application.usecase.GetCurrentUserUseCase;
import com.jame.dev.gymApp.features.current.infrastructure.cache.CurrentCacheValues;
import com.jame.dev.gymApp.features.user.application.support.mapper.RoleMapper;
import com.jame.dev.gymApp.features.user.domain.repository.UserQueryRepository;
import com.jame.dev.gymApp.infrastructure.security.lock.CheckLockProcess;
import com.jame.dev.gymApp.infrastructure.security.lock.LockKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@CheckLockProcess(keys = {LockKeys.PG_RESTORE})
public class GetCurrentUserInfoUseCaseService implements GetCurrentUserUseCase {
   private final UserQueryRepository userQueryRepository;
   private final RoleMapper roleMapper;

   @Override
   @Cacheable(
      value = CurrentCacheValues.CURRENT,
      keyGenerator = "authPrincipalCurrentKeyGen",
      unless = "#result == null"
   )
   public CurrentUserResponse getCurrentUser(AuthPrincipal authPrincipal) {
      return userQueryRepository.findById(authPrincipal.id())
         .map(u -> CurrentUserResponse.builder()
            .name(u.getName())
            .email(u.getEmail())
            .roles(roleMapper.toRoleSet(u.getRoles()))
            .build())
         .orElseThrow(() -> new NotFoundException("Current user not found."));
   }
}
