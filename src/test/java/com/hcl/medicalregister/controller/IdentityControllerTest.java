package com.hcl.medicalregister.controller;

import com.hcl.medicalregister.domain.ADPrincipal;
import com.hcl.medicalregister.domain.dto.AuthenticationRequest;
import com.hcl.medicalregister.domain.dto.AuthenticationResponse;
import com.hcl.medicalregister.repository.ILDAPPrincipalRepository;
import com.hcl.medicalregister.security.JWTTokenUtil;
import com.hcl.medicalregister.service.WUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdentityControllerUnitTest {

    @InjectMocks
    private IdentityController controller;

    @Mock
    private WUserService userService;

    @Mock
    private JWTTokenUtil jwtTokenUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ILDAPPrincipalRepository ldaPrincipalRepository;

    @Test
    void createAuthenticationToken_success_returns200WithTokens() throws Exception {

        AuthenticationRequest req = new AuthenticationRequest();
        req.setUsername("john");
        req.setPassword("secret");

        when(ldaPrincipalRepository.findByCn("john")).thenReturn(mock(ADPrincipal.class));
        UserDetails user = User.withUsername("john").password("x").roles("USER").build();
        when(userService.loadUserByUsername("john")).thenReturn(user);
        when(jwtTokenUtil.generateToken(user)).thenReturn("jwt-token-123");
        when(jwtTokenUtil.generateRefreshToken(user)).thenReturn("refresh-456");


        ResponseEntity<Object> resp = controller.createAuthenticationToken(req);


        assertThat(resp.getStatusCodeValue()).isEqualTo(200);
        assertThat(resp.getBody()).isInstanceOf(Map.class);

        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) resp.getBody();

        assertThat(body.get("Status")).isEqualTo(200);
        assertThat(body.get("Message")).isEqualTo("Authentication");
        assertThat(body.get("Data")).isInstanceOf(AuthenticationResponse.class);

        AuthenticationResponse data = (AuthenticationResponse) body.get("Data");


        assertThat(data.getToken()).isEqualTo("jwt-token-123");


        verify(ldaPrincipalRepository).findByCn("john");
        verify(userService).loadUserByUsername("john");
        verify(jwtTokenUtil).generateToken(user);
        verify(jwtTokenUtil).generateRefreshToken(user);
    }


    @Test
    void createAuthenticationToken_invalidCredentials_returns401() throws Exception {

        AuthenticationRequest req = new AuthenticationRequest();
        req.setUsername("john");
        req.setPassword("wrong");

        when(ldaPrincipalRepository.findByCn("john")).thenReturn(null); // triggers authenticate() failure


        ResponseEntity<Object> resp = controller.createAuthenticationToken(req);


        assertThat(resp.getStatusCode().value()).isEqualTo(401);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().toString()).containsIgnoringCase("invalid");

        verify(ldaPrincipalRepository).findByCn("john");
        verifyNoInteractions(userService, jwtTokenUtil);
        verifyNoMoreInteractions(ldaPrincipalRepository);
    }
}
