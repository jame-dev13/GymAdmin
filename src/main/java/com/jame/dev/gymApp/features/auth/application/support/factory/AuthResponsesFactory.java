package com.jame.dev.gymApp.features.auth.application.support.factory;

import com.jame.dev.gymApp.features.auth.api.response.CookieResponse;
import com.jame.dev.gymApp.features.auth.api.response.SessionResponse;
import com.jame.dev.gymApp.features.auth.api.response.SignInResponse;
import com.jame.dev.gymApp.features.auth.application.contract.JwtService;
import com.jame.dev.gymApp.features.auth.application.model.JwtValidationArgument;
import com.jame.dev.gymApp.features.auth.application.model.LoginUserType;
import com.jame.dev.gymApp.features.auth.application.support.helper.CookieHelper;
import com.jame.dev.gymApp.features.auth.domain.exception.ExtractClaimException;
import com.jame.dev.gymApp.features.auth.domain.exception.InvalidJwtException;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.auth.domain.model.UserPrincipal;
import com.jame.dev.gymApp.features.customer.domain.repository.CustomerValidationRepository;
import com.jame.dev.gymApp.features.user.application.support.mapper.RoleMapper;
import com.jame.dev.gymApp.features.user.domain.model.Role;
import com.jame.dev.gymApp.features.user.domain.model.UserEntity;
import com.jame.dev.gymApp.features.user.domain.repository.UserQueryRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthResponsesFactory {
   private final JwtService jwtService;
   private final CookieHelper cookieHelper;
   private final RoleMapper roleMapper;
   private final UserQueryRepository userRepository;
   private final CustomerValidationRepository customerValidationRepository;

   public CookieResponse createRefreshCookieResponseFrom(@NonNull final String token) {
      final long userId = jwtService.extractUserId(token)
         .orElseThrow(() -> new ExtractClaimException("Cannot extract 'userId' claim."));
      final String subject = jwtService.extractSubject(token)
         .orElseThrow(() -> new ExtractClaimException("Cannot extract 'subject' claim."));
      final boolean isValid = jwtService.isValid(new JwtValidationArgument(token, subject, userId));
      if (!isValid) {
         throw new InvalidJwtException("Not valid Jwt.");
      }
      return generateCookieResponseFrom(userId, subject);
   }

   public CookieResponse generateCookieResponseFrom(long userId, @NonNull final String subject) {
      final String accessToken = jwtService.generateAccessToken(userId, subject);
      final String refreshToken = jwtService.generateRefreshToken(userId, subject);

      final ResponseCookie access = cookieHelper.createAccessTokenCookie(accessToken);
      final ResponseCookie refresh = cookieHelper.createRefreshTokenCookie(refreshToken);
      return new CookieResponse(access.getValue(), refresh.getValue());
   }

   public SignInResponse createSigInResponseFrom(final @NonNull AuthPrincipal principal) {
      if (!(principal instanceof UserPrincipal user))
         throw new IllegalArgumentException("Type of Principal not allowed.");

      final boolean isAdmin = user.getAuthorities()
         .stream()
         .map(GrantedAuthority::getAuthority)
         .anyMatch(ga -> Objects.equals(ga, "ROLE_ADMIN"));

      final CookieResponse cookies = generateCookieResponseFrom(user.id(), user.username());

      if (isAdmin) {
         return SignInResponse.builder()
            .type(LoginUserType.ADMINISTRATIVE)
            .email(user.username())
            .access(cookies.access())
            .refresh(cookies.refresh())
            .build();
      }

      final boolean isCustomer = customerValidationRepository.existsById(user.id());

      return SignInResponse.builder()
         .type(isCustomer ? LoginUserType.CUSTOMER : LoginUserType.USER)
         .email(user.username())
         .access(cookies.access())
         .refresh(cookies.refresh())
         .build();
   }

   public SessionResponse createSessionFrom(String username, Collection<? extends GrantedAuthority> authorities) {
      final Set<Role> roles = roleMapper.authoritiesToRoles(authorities);
      boolean isCustomer = userRepository.findByEmail(username)
         .map(UserEntity::getCustomerEntity)
         .isPresent();
      return new SessionResponse(username, roles, isCustomer);
   }
}
