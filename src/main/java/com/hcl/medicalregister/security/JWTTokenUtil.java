package com.hcl.medicalregister.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTTokenUtil {

	public static final long JWT_TOKEN_VALIDITY = 1 * 60 * 60; // 5 hours

	@Value("${jwt.secret}")
	private String secret;

	// Retrieve username from JWT token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// Retrieve expiration date
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	// General claim fetcher
	public <T> T getClaimFromToken(String token, Function<Claims, T> resolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims != null ? resolver.apply(claims) : null;
	}

	private Claims getAllClaimsFromToken(String token) {
		try {
			return Jwts.parser()
					.setSigningKey(secret.getBytes())
					.parseClaimsJws(token)
					.getBody();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", userDetails.getAuthorities());
		return doGenerateToken(claims, userDetails.getUsername());
	}

	public String generateRefreshToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("role", userDetails.getAuthorities());
		return doGenerateRefreshToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		Map<String, Object> header = new HashMap<>();
		header.put("typ", "JWT");

		return Jwts.builder()
				.setHeader(header)
				.setClaims(claims)
				.setSubject(subject)
				.setIssuer("hcltech")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS256, secret.getBytes())
				.compact();
	}

	private String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
		Map<String, Object> header = new HashMap<>();
		header.put("typ", "JWT");

		return Jwts.builder()
				.setHeader(header)
				.setClaims(claims)
				.setSubject(subject)
				.setIssuer("hcltech")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 2 * 1000))
				.signWith(SignatureAlgorithm.HS256, secret.getBytes())
				.compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
}
