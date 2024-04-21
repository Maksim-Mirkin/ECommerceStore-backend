package edu.mlm.ecommercestore.repository;

import edu.mlm.ecommercestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link User} entities.
 * <p>
 * Manages CRUD operations and complex queries involving {@link User} entities within the e-commerce system.
 * Extending {@link JpaRepository} allows it to inherit a broad range of data access functionalities, including
 * automatic generation of standard database operations. Additionally, this interface defines custom methods
 * for user-specific queries, enhancing the application's ability to efficiently manage user data.
 *
 * @see JpaRepository
 * @see User
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a {@link User} by their username or email, ignoring case.
     * <p>
     * This method enables a flexible and case-insensitive search for users, either by their username or email address.
     * It is particularly useful for authentication and verification processes where the exact case of the user's
     * username or email may not be known or consistent.
     *
     * @param username the username to search for
     * @param email the email to search for
     * @return an {@link Optional} containing the found {@link User} if available; otherwise, an empty {@link Optional}
     */
    Optional<User> findUserByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    /**
     * Finds a {@link User} by their username, ignoring case.
     * <p>
     * Facilitates a case-insensitive search for a user based on their username. This method enhances the system's
     * usability by allowing for username searches that are resilient to case variations, supporting a more
     * user-friendly search and retrieval process.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the found {@link User}, if one exists; otherwise, an empty {@link Optional}
     */
    Optional<User> findUserByUsernameIgnoreCase(String username);
}

