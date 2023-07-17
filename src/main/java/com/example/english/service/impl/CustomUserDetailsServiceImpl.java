package com.example.english.service.impl;

import com.example.english.entities.User;
import com.example.english.entities.principal.UserPrincipal;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
  @Autowired private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findUserByEmail(username)
          .orElseThrow(() -> new NotFoundException("User " + username + " not found"));
    return user;
  }
  @Transactional
  public UserDetails loadUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(
        () -> new ResourceNotFoundException("User " + id + " not found")
    );

    return user;
  }
}
