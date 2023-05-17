package com.example.english.service;

import com.example.english.entities.RefreshToken;
import java.util.Optional;
import org.springframework.security.core.Authentication;

public interface RefreshTokenService {
  RefreshToken createRefreshToken(Authentication authentication);
  RefreshToken verifyExpiration(RefreshToken token);
  Optional<RefreshToken> findByToken(String token);
}
