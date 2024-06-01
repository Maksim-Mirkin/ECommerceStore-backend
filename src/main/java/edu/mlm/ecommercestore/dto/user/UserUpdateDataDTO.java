package edu.mlm.ecommercestore.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating a user's data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for updating a user's data, containing the old username, new username, email, and user image.")
public class UserUpdateDataDTO {

    /**
     * The user's current username. Must be at least 2 characters long.
     */
    @NotNull
    @Size(min = 2)
    @Schema(description = "The current username for the user. Must be at least 2 characters long.",
            example = "user123")
    private String oldUsername;

    /**
     * The user's new username. Must be at least 2 characters long.
     */
    @NotNull
    @Size(min = 2)
    @Schema(description = "The new username for the user. Must be at least 2 characters long.",
            example = "newuser123")
    private String newUsername;

    /**
     * The email address of the user. Must follow standard email format.
     */
    @NotNull
    @Valid
    @Email
    @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @Schema(description = "The email address of the user. Must follow standard email format.",
            example = "user@example.com")
    private String email;

    /**
     * The image URL of the user.
     */
    @NotNull
    @Schema(description = "The URL of the user's image.",
            example = "https://example.com/image.jpg")
    private String userImage;
}
