package com.example.english.mapper;

import com.example.english.dto.request.BlogRequestDTO;
import com.example.english.dto.response.BlogResponseDTO;
import com.example.english.entities.Blog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BlogMapper {
  BlogMapper INSTANCE = Mappers.getMapper(BlogMapper.class);
  @Mapping(target = "image", expression = "java(null)")
  Blog blogRequestDTOToBlog(BlogRequestDTO blogRequestDTO);

  BlogResponseDTO blogToBlogResponseDTO(Blog blog);
}
