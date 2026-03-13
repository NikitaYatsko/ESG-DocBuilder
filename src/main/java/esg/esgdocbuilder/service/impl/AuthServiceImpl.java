package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.constants.ApiErrorMessage;
import esg.esgdocbuilder.mapper.UserMapper;
import esg.esgdocbuilder.model.dto.request.LoginRequest;
import esg.esgdocbuilder.model.dto.response.UserAuthResponse;
import esg.esgdocbuilder.model.entity.RefreshToken;
import esg.esgdocbuilder.model.entity.User;
import esg.esgdocbuilder.repository.UserRepository;
import esg.esgdocbuilder.security.JwtTokenUtils;
import esg.esgdocbuilder.service.AuthService;
import esg.esgdocbuilder.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service

public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserServiceDetails userServiceDetails;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserMapper userMapper;

    @Override
    public UserAuthResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(ApiErrorMessage.BAD_CREDENTIALS.getMessage());
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException(ApiErrorMessage.USER_DOES_NOT_EXIST.getMessage()));

        UserDetails userDetails = userServiceDetails.loadUserByUsername(request.getEmail());
        RefreshToken refreshToken = refreshTokenService.generateOrUpdateRefreshToken(user);
        String token = jwtTokenUtils.generateToken(userDetails);
        return userMapper.getUserAuthResponse(user, refreshToken, token);
    }

    @Override
    public UserAuthResponse refreshAccessToken(String refreshToken) {
        RefreshToken rToken = refreshTokenService.validateAndRefreshToken(refreshToken);
        User user = rToken.getUser();
        UserDetails userDetails = userServiceDetails.loadUserByUsername(user.getEmail());
        String accessToken = jwtTokenUtils.generateToken(userDetails);
        return userMapper.getUserAuthResponse(user, rToken, accessToken);

    }
}