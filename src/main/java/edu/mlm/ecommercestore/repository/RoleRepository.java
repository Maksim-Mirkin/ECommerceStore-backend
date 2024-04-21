package edu.mlm.ecommercestore.repository;

import edu.mlm.ecommercestore.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Role} entities.
 * <p>
 * This interface facilitates CRUD operations and additional data access capabilities for {@link Role} entities
 * within the e-commerce system. By extending {@link JpaRepository}, it leverages Spring Data JPA's powerful
 * abstraction for database operations, simplifying the implementation of data access layers.
 *
 * @see JpaRepository
 * @see Role
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a {@link Role} by its name, ignoring case sensitivity.
     * <p>
     * This method provides a case-insensitive search for a role by its name. It's useful for applications where
     * role names might be input or stored with inconsistent casing, ensuring that role lookup operations are
     * resilient to case variations.
     *
     * @param role the name of the role to find
     * @return an {@link Optional} containing the found {@link Role} if it exists, or an empty {@link Optional} if no match is found
     */
    Optional<Role> findRoleByRoleNameIgnoreCase(String role);
}

