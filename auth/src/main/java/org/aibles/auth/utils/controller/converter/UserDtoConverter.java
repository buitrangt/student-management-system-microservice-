package org.aibles.auth.utils.controller.converter;

import org.aibles.auth.utils.dto.RegisterUserRequest;
import org.aibles.auth.utils.dto.UserProfileResponse;
import org.aibles.auth.utils.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {
    public static User toEntity(RegisterUserRequest request) {
        User userEntity = new User();
        userEntity.setEmail(request.getEmail());
        userEntity.setFullName(request.getFullName());

        return userEntity;
    }
    public static UserProfileResponse entityToResponse(User userEntity) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(userEntity.getUserId());
        response.setFullname(userEntity.getFullName());
        response.setEmail(userEntity.getEmail());
        response.setAddress(userEntity.getAddress());
        response.setRole(userEntity.getRole());
        return response;
    }

}
