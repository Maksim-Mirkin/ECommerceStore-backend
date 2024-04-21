package edu.mlm.ecommercestore.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for conveying basic user information in responses.
 * Includes the user's unique identifier, username, and email address.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for conveying basic user information in responses." +
        " Includes user ID, username, and email address.")
public class UserResponseDTO {

    /**
     * The unique identifier of the user.
     */
    @Schema(description = "The unique identifier of the user.", example = "1")
    private long id;

    /**
     * The username of the user.
     */
    @Schema(description = "The username of the user.", example = "user123")
    private String username;

    /**
     * The email address of the user.
     */
    @Schema(description = "The email address of the user.", example = "user@example.com")
    private String email;
}
