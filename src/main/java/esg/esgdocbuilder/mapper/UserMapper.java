package esg.esgdocbuilder.mapper;

import esg.esgdocbuilder.model.dto.response.UserAuthResponse;
import esg.esgdocbuilder.model.dto.response.UserProfileResponse;
import esg.esgdocbuilder.model.entity.RefreshToken;
import esg.esgdocbuilder.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class UserMapper {
    private final RoleMapper roleMapper;

    public UserProfileResponse getUserProfile(User user) {
        if (user == null) {
            return null;
        }
        UserProfileResponse userProfileResponse = new UserProfileResponse();
        userProfileResponse.setImageUrl(user.getImageUrl());
        userProfileResponse.setEmail(user.getEmail());
        userProfileResponse.setFullName(user.getFullName());
        userProfileResponse.setPhone(user.getPhone());
        userProfileResponse.setCreatedAt(user.getCreatedAt());
        userProfileResponse.setRoles(user.getRoles().stream().map(roleMapper::toDto).collect(Collectors.toList()));
        return userProfileResponse;
    }

    public UserAuthResponse getUserAuthResponse(User user, RefreshToken refreshToken, String accessToken) {
        return UserAuthResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .token(accessToken)
                .refreshToken(refreshToken.getToken())
                .roles(user.getRoles().stream().map(roleMapper::toDto).collect(Collectors.toList()))
                .build();
    }
}
