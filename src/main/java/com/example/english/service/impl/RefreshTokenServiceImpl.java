package com.example.english.service.impl;

import com.example.english.entities.RefreshToken;
import com.example.english.entities.principal.UserPrincipal;
import com.example.english.exceptions.BadRequestException;
import com.example.english.repository.RefreshTokenRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.RefreshTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RefreshTokenRepository refreshTokenRepository;

  @Value("${app.auth.tokenRefreshExpirationMsec}")
  private Long refreshTokenDurationMs;

  @Value("${app.auth.tokenSecret}")
  private String tokenSecret;

  @Override
  public RefreshToken createRefreshToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    RefreshToken refreshToken = new RefreshToken();

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + refreshTokenDurationMs);

    //Create refresh token
    refreshToken.setUser(userRepository.findById(userPrincipal.getId()).get());
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    refreshToken.setToken(Jwts.builder()
        .setSubject(Long.toString(userPrincipal.getId()))
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, tokenSecret)
        .compact());

    refreshToken = refreshTokenRepository.save(refreshToken);
    return refreshToken;
  }

  @Override
  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new BadRequestException("Refresh token was expired. Please make a new signin request with token: " + token.getToken());
    }

    return token;
  }

  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }
}
