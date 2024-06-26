package edu.mlm.ecommercestore.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * Entity representing a user's role within the e-commerce system.
 * Roles are used to enforce access control by defining what actions a user can perform within the system.
 * Common roles include ADMIN for administrative users with full access and CUSTOMER for regular users who
 * interact with the e-commerce platform.
 * <p>
 * Each role is identified by a unique name that follows a specific naming convention to ensure consistency
 * and ease of role management (e.g., ROLE_ADMIN, ROLE_CUSTOMER).
 * <p>
 * This entity maps to the "roles" table in the database, with each role having a unique identifier and a name
 * that adheres to the specified pattern.
 *
 * @author Maksim Mirkin
 * @version 1.0
 * @since 1.0
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "roles")
@Schema(description = "Defines the role of a user within the e-commerce system, such as ADMIN or CUSTOMER, to enforce access control.")
public class Role {

    /**
     * The unique identifier of the role. This ID is auto-generated by the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the role")
    private Long id;

    /**
     * The name of the role, following a specific pattern to ensure consistency across the system.
     * The pattern is defined to start with "ROLE_" followed by uppercase letters (A-Z) and can be up to 20 characters long.
     * This naming convention is important for the integration with Spring Security or similar security frameworks,
     * which often expect role names to follow this format.
     * <p>
     * Include "ROLE_ADMIN" for administrative users and "ROLE_CUSTOMER" for customers of the e-commerce platform.
     */
    @NotNull
    @Pattern(regexp = "^ROLE_[A-Z]{1,20}$")
    @Column(name = "role_name", unique = true, length = 20)
    @Schema(
            description = "The name of the role, adhering to a specific pattern" +
                    " (e.g., ROLE_ADMIN, ROLE_CUSTOMER)",
            example = "ROLE_ADMIN"
    )
    private String roleName;
}
