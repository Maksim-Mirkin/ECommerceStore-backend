package edu.mlm.ecommercestore.service.auth;

import edu.mlm.ecommercestore.dto.login.LoginRequestDTO;
import edu.mlm.ecommercestore.dto.login.LoginResponseDTO;
import edu.mlm.ecommercestore.dto.user.UserRequestDTO;
import edu.mlm.ecommercestore.dto.user.UserResponseDTO;
import edu.mlm.ecommercestore.dto.user.UserUpdateDataDTO;
import edu.mlm.ecommercestore.dto.user.UserUpdatePasswordDTO;
import edu.mlm.ecommercestore.error.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


/**
 * Service interface for authentication and user management operations.
 * <p>
 * Defines the core functionalities related to user authentication, including loading user details,
 * user registration, and login processes. This interface abstracts the authentication logic, providing
 * a clear contract for implementing classes.
 */
public interface AuthService {

    /**
     * Loads the user details by username.
     * <p>
     * This method is primarily used by the authentication process to obtain the user's details
     * based on the username. It throws {@link UsernameNotFoundException} if the user cannot be found,
     * ensuring that the authentication process can respond appropriately to missing or incorrect user information.
     *
     * @param username the username of the user to load
     * @return the {@link UserDetails} of the requested user
     * @throws UsernameNotFoundException if the user with the specified username cannot be found
     */
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * Registers a new user in the system based on the provided user registration data.
     * <p>
     * Handles the processing of user registration requests, including data validation, user creation,
     * and potentially the assignment of default roles. This method facilitates the expansion of the
     * user base by allowing new users to register.
     *
     * @param dto the data transfer object containing the user registration information
     * @return a {@link UserResponseDTO} containing the details of the newly registered user
     */
    UserResponseDTO register(UserRequestDTO dto);

    /**
     * Authenticates a user based on the provided login credentials.
     * <p>
     * Processes login requests by validating the provided credentials against the stored user information.
     * If authentication is successful, this method may also handle the generation of authentication tokens
     * or other mechanisms used to maintain user sessions.
     *
     * @param dto the data transfer object containing the login request information
     * @return a {@link LoginResponseDTO} containing the details relevant to the authenticated session,
     * such as tokens or user information
     */
    LoginResponseDTO login(LoginRequestDTO dto);

    /**
     * Retrieves the details of a user based on the provided username.
     *
     * @param authentication The authentication context of the user performing the operation.
     * @return a {@link UserResponseDTO} containing the user's details.
     * @throws edu.mlm.ecommercestore.error.UsernameNotFoundException if the user with the specified username is not found.
     */
    UserResponseDTO getUserDetails(Authentication authentication);

    /**
     * Updates the user data based on the provided {@link UserUpdateDataDTO}.
     *
     * @param dto the {@link UserUpdateDataDTO} containing the user's new data
     * @return a {@link UserResponseDTO} containing the updated user's details
     * @throws edu.mlm.ecommercestore.error.UsernameNotFoundException if the user with the specified username is not found
     */
    UserResponseDTO updateUserData(UserUpdateDataDTO dto);

    /**
     * Updates the user's password based on the provided {@link UserUpdatePasswordDTO}.
     *
     * @param dto the {@link UserUpdatePasswordDTO} containing the user's current data
     * @return a {@link UserResponseDTO} containing the updated user's details
     * @throws AuthenticationException if the current password does not match or the new password is the same as the current password
     * @throws edu.mlm.ecommercestore.error.UsernameNotFoundException if the user with the specified username is not found
     */
    UserResponseDTO updatePassword(UserUpdatePasswordDTO dto);
}
