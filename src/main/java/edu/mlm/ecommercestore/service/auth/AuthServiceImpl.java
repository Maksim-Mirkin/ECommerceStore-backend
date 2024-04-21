package edu.mlm.ecommercestore.service.auth;

import edu.mlm.ecommercestore.dto.login.LoginRequestDTO;
import edu.mlm.ecommercestore.dto.login.LoginResponseDTO;
import edu.mlm.ecommercestore.dto.user.UserRequestDTO;
import edu.mlm.ecommercestore.dto.user.UserResponseDTO;
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
     * Loads a user's details based on the username, throwing {@link UsernameNotFoundException} if the user does not exist.
     * <p>
     * This method is essential for Spring Security's authentication process, providing user details
     * that include the username, password, and associated roles.
     *
     * @param username the username of the user to load
     * @return a {@link UserDetails} instance representing the user's security details
     * @throws UsernameNotFoundException if the user cannot be found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        val user = getUserEntityOrThrow(username);

        val roles = user.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getRoleName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
    }

    /**
     * Registers a new user with the provided user information, encoding the password and assigning default roles.
     * <p>
     * This method handles new user registration, including validation to ensure that the username and email
     * are unique within the system. It also maps the registration data to a User entity, saves it, and
     * returns a DTO with the user's information.
     *
     * @param dto the data transfer object containing the user registration information
     * @return a {@link UserResponseDTO} with the registered user's details
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
     * Authenticates a user based on the provided credentials, generating a JWT token for successful authentication.
     * <p>
     * This method verifies the user's credentials, throwing {@link AuthenticationException} if authentication fails.
     * Upon successful authentication, it generates and returns a JWT token that the client can use for subsequent
     * authenticated requests.
     *
     * @param dto the data transfer object containing the login credentials
     * @return a {@link LoginResponseDTO} containing the JWT token for the authenticated session
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
                .orElseThrow(() -> {
                    System.out.println("User not found");
                    return new UsernameNotFoundException();
                });
    }
}
