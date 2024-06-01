package edu.mlm.ecommercestore.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for updating a user's password.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for updating a user's password, containing the username, old password, and new password.")
public class UserUpdatePasswordDTO {

    /**
     * The user's username.
     * Must be at least 2 characters long.
     */
    @NotNull
    @Size(min = 2)
    @Schema(description = "The user's username. Must be at least 2 characters long.",
            example = "user123")
    private String username;

    /**
     * The user's current password.
     * Must meet specified security criteria including a mix of digits,
     * lower and uppercase letters, and special characters.
     */
    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*!@$%^&_]).{8,32}$",
            message = "Password must contain at least 1 digit, 1 lowercase letter," +
                    " 1 uppercase letter and 1 special character")
    @Schema(description = "The current password for the user. " +
            "Must meet specified security criteria including a mix of digits, " +
            "lower and uppercase letters, and special characters.",
            example = "SecureP@ss123")
    private String oldPassword;

    /**
     * The user's new password.
     * Must meet specified security criteria including a mix of digits,
     * lower and uppercase letters, and special characters.
     */
    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*!@$%^&_]).{8,32}$",
            message = "Password must contain at least 1 digit, 1 lowercase letter," +
                    " 1 uppercase letter and 1 special character")
    @Schema(description = "The new password for the user. " +
            "Must meet specified security criteria including a mix of digits, " +
            "lower and uppercase letters, and special characters.",
            example = "NewSecureP@ss123")
    private String newPassword;
}
