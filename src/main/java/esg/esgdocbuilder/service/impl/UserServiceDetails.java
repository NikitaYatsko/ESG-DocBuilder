package esg.esgdocbuilder.service.impl;

import esg.esgdocbuilder.constants.ApiErrorMessage;
import esg.esgdocbuilder.model.entity.User;
import esg.esgdocbuilder.repository.RoleRepository;
import esg.esgdocbuilder.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceDetails implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository repository;

    public Optional<User> findByEmail(String username) {
        return userRepository.findByEmail(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(
                ApiErrorMessage.USER_DOES_NOT_EXIST.getMessage()
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                        .toList()
        );

    }

}
