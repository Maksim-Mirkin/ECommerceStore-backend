package edu.mlm.ecommercestore.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents the data transfer object (DTO) for login requests.
 * Contains the necessary credentials for user authentication, including the username and password.
 */
@Schema(description = "Data Transfer Object for a login request." +
        " Includes credentials such as username and password.")
public record LoginRequestDTO(

        @Schema(description = "The username of the user attempting to log in.", example = "user123")
        String username,

        @Schema(
                description = "The password of the user attempting to log in." +
                        " Ensure this is transmitted securely.",
                example = "Passw0rd!"
        )
        String password
) {
}

