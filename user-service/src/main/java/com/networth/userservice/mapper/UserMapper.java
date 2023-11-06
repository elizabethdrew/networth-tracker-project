package com.networth.userservice.mapper;

import com.networth.userservice.dto.UserInput;
import com.networth.userservice.dto.UserOutput;
import com.networth.userservice.entity.User;

public interface UserMapper {

    User toUser(UserInput userInput);

    UserOutput toUserOutput(User user);
}
