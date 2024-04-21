package edu.mlm.ecommercestore.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents the response data transfer object (DTO) for login attempts.
 * This DTO carries the JWT token issued upon successful authentication.
 */
@Schema(description = "Data Transfer Object for the login response." +
        " Contains the JWT token for authenticated sessions.")
public record LoginResponseDTO(
        @Schema(description = "The JWT token issued to the user upon successful authentication.")
        String jwt
) {
}
