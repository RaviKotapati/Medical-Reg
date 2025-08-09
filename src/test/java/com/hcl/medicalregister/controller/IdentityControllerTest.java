package com.hcl.medicalregister.controller;

import com.hcl.medicalregister.domain.dto.AuthenticationRequest;
import com.hcl.medicalregister.domain.dto.AuthenticationResponse;
import com.hcl.medicalregister.security.JWTTokenUtil;
import com.hcl.medicalregister.service.WUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IdentityControllerUnitTest {

    @Mock
    private WUserService userService;

    @Mock
    private JWTTokenUtil jwtTokenUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;


    private IdentityController controller;

    @BeforeEach
    void setUp() {

        controller = new IdentityController(authenticationManager);
        org.springframework.test.util.ReflectionTestUtils.setField(controller, "userService", userService);
        org.springframework.test.util.ReflectionTestUtils.setField(controller, "jwtTokenUtil", jwtTokenUtil);
        org.springframework.test.util.ReflectionTestUtils.setField(controller, "passwordEncoder", passwordEncoder);
    }

    @Test
    void createAuthenticationToken_success() throws Exception {

        String username = "ravi";
        String password = "secret";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(username, password, Collections.emptyList()));

        var userDetails = User.withUsername(username).password("x").authorities(Collections.emptyList()).build();
        when(userService.loadUserByUsername(username)).thenReturn(userDetails);

        when(jwtTokenUtil.generateToken(userDetails)).thenReturn("access-token-123");
        when(jwtTokenUtil.generateRefreshToken(userDetails)).thenReturn("refresh-token-456");

        var req = new AuthenticationRequest();
        req.setUsername(username);
        req.setPassword(password);


        ResponseEntity<Object> response = controller.createAuthenticationToken(req);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);


        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.get("Message")).isEqualTo("Authentication");


        assertThat(String.valueOf(body.get("Status"))).isEqualTo("200");

        Object data = body.get("Data");
        assertThat(data).isInstanceOf(AuthenticationResponse.class);

        AuthenticationResponse auth = (AuthenticationResponse) data;
        assertThat(auth.getToken()).isEqualTo("access-token-123");



        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService).loadUserByUsername(username);
        verify(jwtTokenUtil).generateToken(userDetails);
        verify(jwtTokenUtil).generateRefreshToken(userDetails);
    }


    @Test
    @DisplayName("createAuthenticationToken: returns 401 when credentials are bad")
    void createAuthenticationToken_badCredentials() throws Exception {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("bad"));

        AuthenticationRequest req = new AuthenticationRequest();
        req.setUsername("ravi");
        req.setPassword("wrong");


        ResponseEntity<Object> response = controller.createAuthenticationToken(req);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        assertThat(String.valueOf(response.getBody()))
                .contains("Invalid credentials");

        verify(userService, never()).loadUserByUsername(any());
        verify(jwtTokenUtil, never()).generateToken(any());
        verify(jwtTokenUtil, never()).generateRefreshToken(any());
    }

    @Test
    @DisplayName("createAuthenticationToken: returns 401 when user is disabled")
    void createAuthenticationToken_userDisabled() throws Exception {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new DisabledException("disabled"));

        AuthenticationRequest req = new AuthenticationRequest();
        req.setUsername("ravi");
        req.setPassword("anything");


        ResponseEntity<Object> response = controller.createAuthenticationToken(req);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(String.valueOf(response.getBody()))
                .contains("Invalid credentials");

        verify(userService, never()).loadUserByUsername(any());
        verify(jwtTokenUtil, never()).generateToken(any());
        verify(jwtTokenUtil, never()).generateRefreshToken(any());
    }
}
