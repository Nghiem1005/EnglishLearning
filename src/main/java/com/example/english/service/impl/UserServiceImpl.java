package com.example.english.service.impl;

import com.example.english.dto.request.AuthRequestDTO;
import com.example.english.dto.request.UserRequestDTO;
import com.example.english.dto.response.AuthResponseDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.dto.response.UserResponseDTO;
import com.example.english.entities.Course;
import com.example.english.entities.RefreshToken;
import com.example.english.entities.StudentCourse;
import com.example.english.entities.User;
import com.example.english.entities.enums.AuthProvider;
import com.example.english.entities.enums.Role;
import com.example.english.entities.principal.UserPrincipal;
import com.example.english.exceptions.ResourceAlreadyExistsException;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.UsersMapper;
import com.example.english.repository.CourseRepository;
import com.example.english.repository.FeedbackRepository;
import com.example.english.repository.StudentCourseRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.RefreshTokenService;
import com.example.english.service.UserService;
import com.example.english.utils.TokenProvider;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Autowired private UserRepository userRepository;

  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private JavaMailSender mailSender;

  @Autowired private TokenProvider tokenProvider;

  @Autowired private AuthenticationManager auth;

  @Autowired private RefreshTokenService refreshTokenService;
  @Autowired private StorageServiceImpl imageStorageService;
  @Autowired private CourseRepository courseRepository;
  @Autowired private StudentCourseRepository studentCourseRepository;
  @Autowired private FeedbackRepository feedbackRepository;
  @Override
  public ResponseEntity<?> createUser(UserRequestDTO userRequestDTO, String siteUrl)
      throws UnsupportedEncodingException, MessagingException {
    //Check phone available
    Optional<User> checkUserPhone = userRepository.findUserByPhone(userRequestDTO.getPhone());
    if (checkUserPhone.isPresent()){
      throw new ResourceAlreadyExistsException("Phone user existed");
    }

    //Check email available
    Optional<User> checkUserEmail = userRepository.findUserByEmail(userRequestDTO.getEmail());
    if (checkUserEmail.isPresent()){
      throw new ResourceAlreadyExistsException("Email user existed");
    }

    User user = UsersMapper.MAPPER.userRequestDTOtoUser(userRequestDTO);

    user.setProvider(AuthProvider.local);

    switch (userRequestDTO.getRole().toUpperCase()) {
      case "ADMIN":  {
        user.setRole(Role.ADMIN);
        user.setEnable(true);
        break;
      }
      case "TEACHER" : {
        user.setRole(Role.TEACHER);
        user.setEnable(true);
        break;
      } case "STUDENT" : {
        user.setEnable(false);

        user.setRole(Role.STUDENT);

        String randomCodeVerify = UUID.randomUUID().toString();
        user.setVerificationCode(randomCodeVerify);
        sendVerificationEmail(user, siteUrl);
        break;
      }
      default : {
        throw new ResourceNotFoundException("Could not find role " + userRequestDTO.getRole());
      }
    }

    user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

    UserResponseDTO userResponseDTO = UsersMapper.MAPPER.userToUserResponseDTO(userRepository.save(user));
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create user successfully!", userResponseDTO));
  }

  @Override
  public ResponseEntity<ResponseObject> verifyUser(String verifyCode) {
    User getUser = userRepository.findUserByVerificationCode(verifyCode)
        .orElseThrow(() -> new ResourceNotFoundException("Verify code is incorrect"));
    getUser.setEnable(true);
    User user = userRepository.save(getUser);
    UserResponseDTO userResponseDTO = UsersMapper.MAPPER.userToUserResponseDTO(
        userRepository.save(user));
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Verify account success!!!", userResponseDTO));
  }

  @Override
  public ResponseEntity<?> login(AuthRequestDTO authRequestDTO) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(), authRequestDTO.getPassword());
    Authentication authentication = auth.authenticate(authenticationToken);
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

    String accessToken = tokenProvider.createToken(authentication);

    RefreshToken refreshToken = refreshTokenService.createRefreshToken(authentication);

    AuthResponseDTO authResponseDTO = new AuthResponseDTO(userPrincipal.getId(), accessToken, refreshToken.getToken());
    return ResponseEntity.ok().body(new ResponseObject(HttpStatus.OK, "Login Successfully", authResponseDTO));
  }

  @Override
  public ResponseEntity<?> getUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));
    UserResponseDTO userResponseDTO = UsersMapper.MAPPER.userToUserResponseDTO(user);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Success", userResponseDTO));
  }

  @Override
  public ResponseEntity<?> updateUser(Long id, UserRequestDTO userRequestDTO) throws IOException {
    //Check user existed
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));

    //Check email user existed
    if (userRequestDTO.getEmail() != null){
      if (!userRequestDTO.getEmail().equals(user.getEmail())){
        Optional<User> userCheckEmail = userRepository.findUserByEmail(userRequestDTO.getEmail());
        if (userCheckEmail.isPresent()) {
          throw new ResourceAlreadyExistsException("Email user existed");
        }
      }
      user.setEmail(userRequestDTO.getEmail());
    }

    //Check phone user existed
    if (userRequestDTO.getPhone() != null){
      if (!userRequestDTO.getPhone().equals(user.getPhone())){
        Optional<User> userCheckPhone = userRepository.findUserByPhone(userRequestDTO.getPhone());
        if (userCheckPhone.isPresent()) {
          throw new ResourceAlreadyExistsException("Phone user existed");
        }
      }
      user.setPhone(userRequestDTO.getPhone());
    }

    //Storage image
    if (userRequestDTO.getImage() != null) {
      user.setImages(
          imageStorageService.uploadFile(userRequestDTO.getImage()));
    }

    //Set enable
    user.setEnable(userRequestDTO.isEnable());

    //Set name
    if (userRequestDTO.getName() != null){
      user.setName(userRequestDTO.getName());
    }

    UserResponseDTO userResponseDTO =
        UsersMapper.MAPPER.userToUserResponseDTO(userRepository.save(user));

    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Update user successfully!",
            userResponseDTO));
  }

  @Override
  public ResponseEntity<?> updatePassword(Long userId, String newPassword, String oldPassword) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));

    //Check password
    String password = passwordEncoder.encode(oldPassword);
    if (!passwordEncoder.matches(oldPassword, user.getPassword())){
      throw new ResourceNotFoundException("Mật khẩu tài khoản không chính xác");
    }

    //Update new password
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);

    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update password success"));
  }

  @Override
  public ResponseEntity<?> deleteUser(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + id));

    if (user.getRole() == Role.TEACHER) {
      //Update teacher of course to null
      List<Course> courseList = courseRepository.findCoursesByTeacher(user);
      for (Course course : courseList) {
        course.setTeacher(null);
        courseRepository.save(course);
      }
    } else {
      //Delete course of student
      List<StudentCourse> studentCourseList = studentCourseRepository.findStudentCoursesByUser(
          user);
      studentCourseRepository.deleteAll(studentCourseList);
    }

    if (!(user.getImages() == null)){
      imageStorageService.deleteFile(user.getImages());
    }
    userRepository.delete(user);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new ResponseObject(HttpStatus.OK, "Delete user success!!!", null));
  }

  private void sendVerificationEmail(User user, String siteUrl)
      throws MessagingException, UnsupportedEncodingException {
    //Design mail form
    String subject = "Please verify your registration";
    String senderName = "Mobile University Store";
    String verifyUrl = siteUrl + "/verify?code=" + user.getVerificationCode();
    String mailContent = "<p>Dear " + user.getName() + ",<p><br>"
        + "Please click the link below to verify your registration:<br>"
        + "<h3><a href = \"" + verifyUrl + "\">VERIFY</a></h3>"
        + "Thank you,<br>" + "Mobile University.";

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper messageHelper = new MimeMessageHelper(message);

    messageHelper.setFrom("englishweb@gmail.com", senderName);
    messageHelper.setTo(user.getEmail());
    messageHelper.setSubject(subject);
    messageHelper.setText(mailContent, true);
    mailSender.send(message);
  }
}
