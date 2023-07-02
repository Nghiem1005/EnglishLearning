package com.example.english.controller;

import com.example.english.dto.request.UserRequestDTO;
import com.example.english.service.UserService;
import com.example.english.service.impl.StorageServiceImpl;
import java.io.IOException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/v1/user")
public class UserController {
  @Autowired
  private UserService userService;
  @Autowired private StorageServiceImpl imageStorageService;

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getUserById(@PathVariable(name = "id") Long id){
    return userService.getUserById(id);
  }

  @GetMapping(value = "/teacher")
  public ResponseEntity<?> getAllTeacher(){
    return userService.getAllTeacher();
  }

  @PutMapping(value = "/password/{userId}")
  public ResponseEntity<?> updateUser(@RequestPart(name = "newPassword") String newPassword,
      @PathVariable(name = "userId") Long userId, @RequestPart(name = "oldPassword") String oldPassword){
    return userService.updatePassword(userId, newPassword, oldPassword);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<?> updateUser(@PathVariable(name = "id") Long id, @ModelAttribute @Valid UserRequestDTO userRequestDTO)
      throws IOException {
    return userService.updateUser(id, userRequestDTO);
  }

  @PostMapping(value = "/image")
  public ResponseEntity<?> updateImageUser(@RequestPart MultipartFile image) throws IOException {
    String imgLink = imageStorageService.uploadFile(image);
    return ResponseEntity.status(HttpStatus.OK).body(imgLink);
  }

  @DeleteMapping(value = "/{id}")
  public ResponseEntity<?> deleteUser(@PathVariable(name = "id") Long id){
    return userService.deleteUser(id);
  }

}
