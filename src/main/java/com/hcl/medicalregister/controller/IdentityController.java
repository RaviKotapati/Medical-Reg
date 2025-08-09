package com.hcl.medicalregister.controller;

import com.hcl.medicalregister.domain.ADPrincipal;
import com.hcl.medicalregister.domain.dto.AuthenticationRequest;
import com.hcl.medicalregister.domain.dto.AuthenticationResponse;
import com.hcl.medicalregister.domain.dto.EntityResponse;
import com.hcl.medicalregister.security.JWTTokenUtil;
import com.hcl.medicalregister.service.WUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("identity")
public class IdentityController {

	@Autowired
	WUserService userService;

	@Autowired
	private JWTTokenUtil jwtTokenUtil;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager;

	public IdentityController(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/token")
	public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		} catch (Exception e) {
			return EntityResponse.generateResponse("Authentication", HttpStatus.UNAUTHORIZED,
					"Invalid credentials, please check details and try again.");
		}
		final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());
		System.out.println("userDetails :: "+userDetails);
		final String token = jwtTokenUtil.generateToken(userDetails);
		final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

		return EntityResponse.generateResponse("Authentication", HttpStatus.OK,
				new AuthenticationResponse(token, refreshToken));

	}

	private void authenticate(String username, String password) throws Exception {
		try {
			var auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password));
			if(auth == null) {
				throw new Exception("Invalid credentials");
			}
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			e.printStackTrace();
			throw new Exception("INVALID_CREDENTIALS", e);
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception("INVALID_CREDENTIALS", e.getCause());
			
		}
	}
	

}
