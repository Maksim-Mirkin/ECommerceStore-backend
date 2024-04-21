package edu.mlm.ecommercestore.controller;

import edu.mlm.ecommercestore.dto.exception.ExceptionDTO;
import edu.mlm.ecommercestore.dto.exception.InternalServerExceptionDTO;
import edu.mlm.ecommercestore.dto.login.LoginRequestDTO;
import edu.mlm.ecommercestore.dto.login.LoginResponseDTO;
import edu.mlm.ecommercestore.dto.user.UserDetailsResponseDTO;
import edu.mlm.ecommercestore.dto.user.UserRequestDTO;
import edu.mlm.ecommercestore.dto.user.UserResponseDTO;
import edu.mlm.ecommercestore.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Controller for managing authentication operations, including login and registration of users.
 * It defines endpoints for user login and registration processes.
 *
 * @author Maksim Mirkin
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {
    private final AuthService authService;

    /**
     * Authenticates a user and returns a login token if successful.
     *
     * @param dto The login request data transfer object containing username and password.
     * @return {@link LoginResponseDTO} containing the login response, which includes authentication token.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerExceptionDTO.class)
                    )
            )
    })
    @Operation(summary = "User login",
            description = "Authenticates user with provided credentials and " +
                    "returns an authentication token if successful.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    /**
     * Retrieves the details of the currently authenticated user.
     *
     * @param authentication The Authentication object providing context about the currently authenticated user.
     * @return {@link UserDetailsResponseDTO} containing the details of the authenticated user.
     */
    @Hidden
    @Operation(summary = "Get current user details",
            description = "Retrieves details of the currently authenticated user.")
    @GetMapping("/me")
    public ResponseEntity<UserDetailsResponseDTO> userDetails(Authentication authentication) {
        var res = UserDetailsResponseDTO.builder()
                .username(authentication.getName())
                .roles(authentication.getAuthorities())
                .build();
        return ResponseEntity.ok(res);
    }

    /**
     * Registers a new user in the system.
     *
     * @param dto The user registration request data transfer object containing user details.
     * @param uriBuilder A UriComponentsBuilder for building the URI of the newly created resource.
     * @return {@link UserResponseDTO} containing the registered user's details and location header of the login endpoint.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400",
                    description = "User already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerExceptionDTO.class)
                    )
            )
    })
    @Operation(summary = "User registration",
            description = "Registers a new user in the system with the provided user details.")
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(
            @RequestBody @Valid UserRequestDTO dto,
            UriComponentsBuilder uriBuilder
    ) {
        return ResponseEntity
                .created(uriBuilder.path("/login").build().toUri())
                .body(authService.register(dto));
    }
}
