package edu.mlm.ecommercestore.service.auth;

import edu.mlm.ecommercestore.dto.login.LoginRequestDTO;
import edu.mlm.ecommercestore.dto.login.LoginResponseDTO;
import edu.mlm.ecommercestore.dto.user.UserRequestDTO;
import edu.mlm.ecommercestore.dto.user.UserResponseDTO;
import edu.mlm.ecommercestore.dto.user.UserUpdateDataDTO;
import edu.mlm.ecommercestore.dto.user.UserUpdatePasswordDTO;
import edu.mlm.ecommercestore.entity.User;
import edu.mlm.ecommercestore.error.AuthenticationException;
import edu.mlm.ecommercestore.error.UserAlreadyExistsException;
import edu.mlm.ecommercestore.error.UsernameNotFoundException;
import edu.mlm.ecommercestore.repository.RoleRepository;
import edu.mlm.ecommercestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link AuthService} interface, providing concrete functionalities for user authentication,
 * registration, and retrieval of user details.
 * <p>
 * This service leverages the Spring Security framework for authentication tasks, utilizes JPA repositories for
 * user and role management, and employs a JWT service for token generation and validation. It also uses
 * {@link ModelMapper} for object mapping between entities and DTOs, facilitating data transfer within the application.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val user = getUserEntityOrThrow(username);

        val roles = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRoleName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResponseDTO register(UserRequestDTO dto) {
        userRepository.findUserByUsernameIgnoreCaseOrEmailIgnoreCase(dto.getUsername(), dto.getEmail())
                .ifPresent((u) -> {
                    throw new UserAlreadyExistsException(u.getUsername(), u.getEmail());
                });
        var user = modelMapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        val role = roleRepository.findRoleByRoleNameIgnoreCase("ROLE_CUSTOMER").orElseThrow();
        user.setRoles(Set.of(role));

        val saved = userRepository.save(user);
        return modelMapper.map(saved, UserResponseDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        val user = getUserEntityOrThrow(dto.username());

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new AuthenticationException();
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                dto.password(),
                user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRoleName())).toList()
        );

        val jwt = jwtService.jwtToken(authentication);

        return new LoginResponseDTO(jwt);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResponseDTO getUserDetails(Authentication authentication) {
        val user = getUserEntityOrThrow(authentication.getName());
        return modelMapper.map(user, UserResponseDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResponseDTO updateUserData(UserUpdateDataDTO dto) {
        val user = getUserEntityOrThrow(dto.getOldUsername());
        user.setUsername(dto.getNewUsername());
        user.setEmail(dto.getEmail());
        user.setUserImage(dto.getUserImage());
        val saved = userRepository.save(user);
        return modelMapper.map(saved, UserResponseDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResponseDTO updatePassword(UserUpdatePasswordDTO dto) throws AuthenticationException {
        val user = getUserEntityOrThrow(dto.getUsername());
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new AuthenticationException("Provide password does not match the current password.");
        }
        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new AuthenticationException("New password cannot be the same as the current password.");
        }
        val encodedNewPassword = passwordEncoder.encode(dto.getNewPassword());
        user.setPassword(encodedNewPassword);
        val saved = userRepository.save(user);
        return modelMapper.map(saved, UserResponseDTO.class);
    }

    /**
     * Retrieves a {@link User} entity by username, case-insensitively, throwing {@link UsernameNotFoundException}
     * if the user does not exist.
     * <p>
     * This private helper method is used internally to fetch user entities based on the username, supporting
     * the loadUserByUsername and login methods by centralizing user retrieval logic.
     *
     * @param username the username of the user to retrieve
     * @return a {@link User} entity corresponding to the given username
     * @throws UsernameNotFoundException if the user with the given username cannot be found
     */
    private User getUserEntityOrThrow(String username) {
        return userRepository.findUserByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Provided username '%s' is not exist", username)));
    }
}
