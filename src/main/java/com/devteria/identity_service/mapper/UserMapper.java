package com.devteria.identity_service.mapper;

import com.devteria.identity_service.dtos.requests.UserCreationRequest;
import com.devteria.identity_service.dtos.requests.UserUpdateRequest;
import com.devteria.identity_service.dtos.responses.UserResponse;
import com.devteria.identity_service.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class})
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "username", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
