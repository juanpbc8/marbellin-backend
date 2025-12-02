package com.marbellin.auth.mapper;

import com.marbellin.auth.dto.UserDto;
import com.marbellin.auth.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roleName", expression = "java(entity.getRole().getRoleName().name())")
    UserDto toDto(UserEntity entity);
}

