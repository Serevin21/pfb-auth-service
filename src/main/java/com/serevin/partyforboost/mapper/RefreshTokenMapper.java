package com.serevin.partyforboost.mapper;


import com.serevin.partyforboost.entity.RefreshToken;
import com.serevin.partyforboost.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface RefreshTokenMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "deviceAgent", source = "deviceAgent")
    @Mapping(target = "token", source = "token")
    @Mapping(target = "expiryAt", source = "expiryAt")
    RefreshToken map(User user, String deviceAgent, String token, LocalDateTime expiryAt);

}
