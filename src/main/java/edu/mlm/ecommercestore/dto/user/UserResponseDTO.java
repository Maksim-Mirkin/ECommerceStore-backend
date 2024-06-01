package edu.mlm.ecommercestore.dto.user;

import edu.mlm.ecommercestore.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Data Transfer Object for conveying basic user information in responses.
 * Includes the user's unique identifier, username, user image and email address.
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

    /**
     * The image of the user.
     */
    @Schema(description = "The image of the user.", example = "https://example.com/image.jpg")
    private String userImage;

    /**
     * The roles of the user.
     */
    @Schema(description = "The roles of the user.", example = "ROLE_USER")
    private Set<Role> roles;
}
