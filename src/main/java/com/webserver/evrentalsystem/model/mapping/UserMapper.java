package com.webserver.evrentalsystem.model.mapping;

import com.webserver.evrentalsystem.entity.User;
import com.webserver.evrentalsystem.model.dto.entitydto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUserDto(User user);
}
