package com.networth.userservice.mapper;

import com.networth.userservice.dto.LoginResponse;
import com.networth.userservice.dto.TokenResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TokenResponseMapper {
    LoginResponse tokenResponseToLoginResponse(TokenResponse tokenResponse);
}
