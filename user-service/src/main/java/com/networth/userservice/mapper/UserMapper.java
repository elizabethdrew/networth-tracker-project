package com.networth.userservice.mapper;

import com.networth.userservice.dto.RegisterDto;
import com.networth.userservice.dto.UpdateUserDto;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(RegisterDto registerDto);

    User toUpdateUser(UpdateUserDto updateUserDto);

    void updateUserFromDto(UpdateUserDto dto, @MappingTarget User user);

    UserOutput toUserOutput(User user);
}
