package com.jame.dev.gymApp.service;

import com.jame.dev.gymApp.features.auth.api.response.SessionResponse;
import com.jame.dev.gymApp.features.auth.application.model.AuthProvider;
import com.jame.dev.gymApp.features.auth.application.service.SessionApplicationService;
import com.jame.dev.gymApp.features.auth.application.support.factory.AuthResponsesFactory;
import com.jame.dev.gymApp.features.auth.domain.model.CustomOAuth2User;
import com.jame.dev.gymApp.features.auth.domain.model.UserPrincipal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {
   private static final Collection<GrantedAuthority> AUTHORITIES =
      List.of(new SimpleGrantedAuthority("ROLE_USER"));

   @Mock
   private AuthResponsesFactory authResponsesFactory;

   @InjectMocks
   private SessionApplicationService service;

   @Test
   @DisplayName("Should get session from a UserPrincipal object.")
   void shouldGetSessionDto() {
      final SessionResponse response = mock(SessionResponse.class);
      final UserPrincipal principal = UserPrincipal.builder()
         .id(1L)
         .username("john.doe")
         .password("encoded")
         .authorities(AUTHORITIES)
         .build();

      given(authResponsesFactory.createSessionFrom(principal.username(), principal.getAuthorities()))
         .willReturn(response);

      final var session = assertDoesNotThrow(() -> service.getSession(principal), "Shouldn't have throws exception.");

      verify(authResponsesFactory).createSessionFrom(principal.username(), principal.getAuthorities());
      verifyNoMoreInteractions(authResponsesFactory);

      assertSame(response, session, "Session should be the one returned by the factory.");
   }

   @Test
   @DisplayName("Should get session from a CustomOauth2User object.")
   void getSession_from_CustomOauth2User() {
      final SessionResponse response = mock(SessionResponse.class);
      final CustomOAuth2User principal = CustomOAuth2User.builder()
         .id(1L)
         .username("john.doe")
         .provider(AuthProvider.GOOGLE)
         .attributes(Map.of())
         .authorities(AUTHORITIES)
         .build();

      given(authResponsesFactory.createSessionFrom(principal.username(), principal.getAuthorities()))
         .willReturn(response);

      final var session = assertDoesNotThrow(() -> service.getSession(principal), "Shouldn't have throws exception.");

      verify(authResponsesFactory).createSessionFrom(principal.username(), principal.getAuthorities());
      verifyNoMoreInteractions(authResponsesFactory);

      assertSame(response, session, "Session should be the one returned by the factory.");
   }

   @Test
   @DisplayName("Should throws exception when AuthPrincipal's null.")
   void shouldThrowsAuthenticationNullException() {
      assertThrowsExactly(NullPointerException.class, () -> service.getSession(null), "Should throws exactly NPE.");

      verifyNoInteractions(authResponsesFactory);
   }
}
