package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.constants.ApiErrorMessage;
import esg.esgdocbuilder.exception.exceptions.PasswordDoNotMatchException;
import esg.esgdocbuilder.exception.exceptions.RoleNotFoundExecption;
import esg.esgdocbuilder.exception.exceptions.UserAlreadyExistsException;
import esg.esgdocbuilder.exception.exceptions.UserDoesNotExistsException;
import esg.esgdocbuilder.mapper.UserMapper;
import esg.esgdocbuilder.model.dto.request.UserRegistrationRequest;
import esg.esgdocbuilder.model.dto.response.UserProfileResponse;
import esg.esgdocbuilder.model.entity.Role;
import esg.esgdocbuilder.model.entity.User;
import esg.esgdocbuilder.repository.RoleRepository;
import esg.esgdocbuilder.repository.UserRepository;
import esg.esgdocbuilder.service.RefreshTokenService;
import esg.esgdocbuilder.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserProfileResponse createNewUser(UserRegistrationRequest request) {
        log.info("Received registration request: email={}, password={}, confirmPassword={}",
                request.getEmail(),
                request.getPassword(),
                request.getConfirmPassword());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException(ApiErrorMessage.USER_ALREADY_EXISTS.getMessage());
        }
        if (request.getPassword() == null || request.getConfirmPassword() == null
                || !request.getPassword().equals(request.getConfirmPassword())) {
            throw new PasswordDoNotMatchException(ApiErrorMessage.PASSWORD_DOES_NOT_MATCH.getMessage());
        }
        User newUser = new User();
        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RoleNotFoundExecption(ApiErrorMessage.ROLE_NOT_FOUND.getMessage()));

        newUser.setEmail(request.getEmail());
        newUser.setFirstName(request.getFirstName());
        newUser.setPhone(request.getPhone());
        newUser.setLastName(request.getLastName());
        newUser.setPhone(request.getPhone());
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        newUser.setRoles(roles);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(newUser);
        return userMapper.getUserProfile(newUser);


    }


    @Override
    public List<UserProfileResponse> findAll() {
        return userRepository.findAll().stream().map(userMapper::getUserProfile).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserDoesNotExistsException(ApiErrorMessage.USER_DOES_NOT_EXIST.getMessage()));

        userRepository.delete(user);

    }

}
