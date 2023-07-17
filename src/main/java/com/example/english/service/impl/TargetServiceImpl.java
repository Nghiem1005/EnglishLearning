package com.example.english.service.impl;

import com.example.english.dto.request.TargetRequestDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.dto.response.TargetResponseDTO;
import com.example.english.dto.response.UserResponseDTO;
import com.example.english.entities.Target;
import com.example.english.entities.User;
import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.mapper.TargetMapper;
import com.example.english.mapper.UsersMapper;
import com.example.english.repository.TargetRepository;
import com.example.english.repository.UserRepository;
import com.example.english.service.TargetService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TargetServiceImpl implements TargetService {
  @Autowired private UserRepository userRepository;
  @Autowired private TargetRepository targetRepository;
  @Override
  public ResponseEntity<?> createTarget(Long userId, TargetRequestDTO targetRequestDTO) {
    User student = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userId));

    Target target = TargetMapper.INSTANCE.targetRequestDTOToTarget(targetRequestDTO);
    target.setUser(student);

    TargetResponseDTO targetResponseDTO = TargetMapper.INSTANCE.targetToTargetResponseDTO(targetRepository.save(target));
    UserResponseDTO userResponseDTO = UsersMapper.MAPPER.userToUserResponseDTO(student);
    targetResponseDTO.setUserResponseDTO(userResponseDTO);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Create target success!", targetResponseDTO));
  }

  @Override
  public ResponseEntity<?> getTargetByUser(Long userID) {
    User student = userRepository.findById(userID)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find user with ID = " + userID));

    Optional<Target> target = targetRepository.findTargetByUser(student);

    if (target.isEmpty()) {
      throw new ResourceNotFoundException("User not have target");
    }

    TargetResponseDTO targetResponseDTO = TargetMapper.INSTANCE.targetToTargetResponseDTO(target.get());
    UserResponseDTO userResponseDTO = UsersMapper.MAPPER.userToUserResponseDTO(target.get().getUser());
    targetResponseDTO.setUserResponseDTO(userResponseDTO);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Get target success!", targetResponseDTO));
  }

  @Override
  public ResponseEntity<?> updateTarget(Long targetId, TargetRequestDTO targetRequestDTO) {
    Target target = targetRepository.findById(targetId)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find target with ID = " + targetId));

    if (targetRequestDTO.getType() != null) {
      target.setType(targetRequestDTO.getType());
    }

    if (targetRequestDTO.getPoint() != 0) {
      target.setPoint(targetRequestDTO.getPoint());
    }

    TargetResponseDTO targetResponseDTO = TargetMapper.INSTANCE.targetToTargetResponseDTO(targetRepository.save(target));
    UserResponseDTO userResponseDTO = UsersMapper.MAPPER.userToUserResponseDTO(target.getUser());
    targetResponseDTO.setUserResponseDTO(userResponseDTO);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Update target success!", targetResponseDTO));
  }

  @Override
  public ResponseEntity<?> deleteTarget(Long id) {
    Target target = targetRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Could not find target with ID = " + id));

    targetRepository.delete(target);
    return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(HttpStatus.OK, "Delete target success!"));
  }
}
