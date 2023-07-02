package com.example.english.service;

import com.example.english.dto.request.AuthRequestDTO;
import com.example.english.dto.request.UserRequestDTO;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import org.springframework.http.ResponseEntity;

public interface UserService {
  ResponseEntity<?> createUser(UserRequestDTO userRequestDTO, String siteUrl)
      throws MessagingException, UnsupportedEncodingException;
  ResponseEntity<?> verifyUser(String verifyCode);
  ResponseEntity<?> getAllTeacher();
  ResponseEntity<?> login(AuthRequestDTO authRequestDTO);
  ResponseEntity<?> getUserById(Long id);
  ResponseEntity<?> updateUser(Long id, UserRequestDTO userRequestDTO) throws IOException;
  ResponseEntity<?> updatePassword(Long userId, String newPassword, String oldPassword);
  ResponseEntity<?> deleteUser(Long id);
}
