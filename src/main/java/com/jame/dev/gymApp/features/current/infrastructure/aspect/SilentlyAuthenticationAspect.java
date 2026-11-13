package com.jame.dev.gymApp.features.current.infrastructure.aspect;

import com.jame.dev.gymApp.features.auth.application.contract.JwtService;
import com.jame.dev.gymApp.features.auth.application.contract.LogoutService;
import com.jame.dev.gymApp.features.auth.application.support.helper.CookieHelper;
import com.jame.dev.gymApp.features.auth.domain.exception.AuthenticationAttemptFailureException;
import com.jame.dev.gymApp.features.auth.domain.model.AuthPrincipal;
import com.jame.dev.gymApp.features.current.application.model.CurrentUserModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SilentlyAuthenticationAspect {

   private final UserDetailsService userDetailsService;
   private final JwtService jwtService;
   private final LogoutService logoutService;
   private final CookieHelper cookieHelper;

   @Around(
      value = "@annotation(com.jame.dev.gymApp.features.current.infrastructure.annotation.MakeSilentlyAuthentication) && args(.., request, response)",
      argNames = "jp,request,response"
   )
   public Object silentlyAuthenticateUser(
      final ProceedingJoinPoint jp,
      final HttpServletRequest request,
      final HttpServletResponse response
   ) throws Throwable {
      try {
         final Object result = jp.proceed();

         if (!(result instanceof CurrentUserModel model)) {
            throw new IllegalArgumentException("resulting type unexpected.");
         }

         if (!model.isCredentialsModified()) return result;

         logoutService.logout(request, response);
         log.info("Logout made.");

         final UserDetails details = userDetailsService.loadUserByUsername(model.email());

         log.info("user details located: {}", details);

         final Authentication authentication = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());

         log.info("New authentication token created, {}", authentication);

         SecurityContextHolder.getContext().setAuthentication(authentication);

         log.info("Context updated.");

         final Object principal = Objects.requireNonNull(authentication.getPrincipal(), "Something went wrong: User principal not found.");

         if (!(principal instanceof AuthPrincipal authPrincipal)) {
            throw new AuthenticationAttemptFailureException("Could not reauthenticate user.");
         }

         final String newAccess = jwtService.generateAccessToken(authPrincipal.id(), authPrincipal.username());
         final String newRefresh = jwtService.generateRefreshToken(authPrincipal.id(), authPrincipal.username());

         final var accessCookie = cookieHelper.createAccessTokenCookie(newAccess);
         final var refreshCookie = cookieHelper.createRefreshTokenCookie(newRefresh);

         response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
         response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

         log.info("Settled down new cookies");

         return result;
      } catch (Throwable th) {
         log.error("Could not silently authenticate user. ", th);
         throw th;
      }
   }
}
