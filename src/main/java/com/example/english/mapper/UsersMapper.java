package com.example.english.mapper;

import com.example.english.dto.request.UserRequestDTO;
import com.example.english.dto.response.UserResponseDTO;
import com.example.english.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsersMapper {
  UsersMapper MAPPER = Mappers.getMapper(UsersMapper.class);
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "email", source = "dto.email")
  @Mapping(target = "phone", source = "dto.phone")
  @Mapping(target = "role", expression = "java(null)")
  User userRequestDTOtoUser(UserRequestDTO dto);

  @Mapping(target = "id", source = "user.id")
  @Mapping(target = "name", source = "user.name")
  @Mapping(target = "email", source = "user.email")
  @Mapping(target = "phone", source = "user.phone")
  @Mapping(target = "enable", source = "user.enable")
  @Mapping(target = "image", source = "user.images")
  @Mapping(target = "role", source = "user.role")
  @Mapping(target = "provider", source = "user.provider")
  UserResponseDTO userToUserResponseDTO(User user);
}
