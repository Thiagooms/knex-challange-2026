package com.knex.backend.mappers;

import com.knex.backend.dtos.user.UserResponseDto;
import com.knex.backend.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface UserMapper {

    @Mapping(target = "role", expression = "java(user.getRole().name())")
    @Mapping(target = "company", source = "company")
    UserResponseDto toResponseDto(User user);
}
