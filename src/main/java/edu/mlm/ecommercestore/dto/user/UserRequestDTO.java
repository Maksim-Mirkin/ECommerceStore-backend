package edu.mlm.ecommercestore.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Data Transfer Object for user registration or update requests.
 * Includes essential user information such as username, email, and password with specific validation rules.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for user registration or update requests." +
        " Specifies user details including validation for each field.")
public class UserRequestDTO {

    /**
     * The username for the user. Must be at least 2 characters long.
     */
    @NotNull
    @Size(min = 2)
    @Schema(description = "The username for the user. Must be at least 2 characters long.",
            example = "user123")
    private String username;

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
     * The password for the user. Must meet specified security criteria.
     */
    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*!@$%^&_]).{8,32}$",
            message = "Password must contain at least 1 digit, 1 lowercase letter," +
                    " 1 uppercase letter and 1 special character")
    @Schema(description = "The password for the user." +
            " Must meet specified security criteria including a mix of digits," +
            " lower and uppercase letters, and special characters.",
            example = "SecureP@ss123")
    private String password;
}
