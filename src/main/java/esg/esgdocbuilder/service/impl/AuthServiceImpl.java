package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.model.dto.RoleDTO;
import esg.esgdocbuilder.model.dto.request.LoginRequest;
import esg.esgdocbuilder.model.dto.response.UserProfileDTO;
import esg.esgdocbuilder.model.entity.User;
import esg.esgdocbuilder.repository.UserRepository;
import esg.esgdocbuilder.security.JwtTokenUtils;
import esg.esgdocbuilder.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service

public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserServiceDetails userServiceDetails;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepository userRepository;

    @Override
    public UserProfileDTO login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        UserDetails userDetails =
                userServiceDetails.loadUserByUsername(request.getEmail());

        String token = jwtTokenUtils.generateToken(userDetails);

        List<RoleDTO> roles = user.getRoles().stream()
                .map(role -> new RoleDTO( role.getName()))
                .collect(Collectors.toList());

        return new UserProfileDTO(
                user.getEmail(),
                user.getFullName(),
                token,
                roles
        );
    }
}