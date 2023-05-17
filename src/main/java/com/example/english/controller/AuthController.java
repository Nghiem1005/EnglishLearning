package com.example.english.controller;

import com.example.english.dto.request.AuthRequestDTO;
import com.example.english.dto.request.UserRequestDTO;
import com.example.english.service.UserService;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "")
public class AuthController {
  @Autowired
  private UserService userService;

  @PostMapping(value = "/register")
  public ResponseEntity<?> register(@ModelAttribute @Valid UserRequestDTO userRequestDTO, HttpServletRequest request)
      throws MessagingException, UnsupportedEncodingException {
    String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
    return userService.createUser(userRequestDTO, siteUrl);
  }

  @GetMapping(value = "/verify")
  public ResponseEntity<?> verifyUser(@RequestParam(name = "code") String verifyCode){
    return userService.verifyUser(verifyCode);
  }

  @PostMapping(value = "/login/student")
  public ResponseEntity<?> loginUser(@RequestBody AuthRequestDTO authRequestDTO) {
    return userService.login(authRequestDTO);
  }

  @PostMapping(value = "/login/teacher")
  public ResponseEntity<?> loginTeacher(@RequestBody AuthRequestDTO authRequestDTO) {
    return userService.login(authRequestDTO);
  }

  @PostMapping(value = "/login/admin")
  public ResponseEntity<?> loginAdmin(@RequestBody AuthRequestDTO authRequestDTO) {
    return userService.login(authRequestDTO);
  }
}
